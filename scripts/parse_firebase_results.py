#!/usr/bin/env python3
"""
Firebase Test Lab Results Parser
Extracts and summarizes crash logs, test failures, and performance metrics from Firebase Test Lab results.
"""

import os
import sys
import json
import xml.etree.ElementTree as ET
import re
from pathlib import Path
from typing import Dict, List, Tuple, Optional
from datetime import datetime

class FirebaseResultParser:
    def __init__(self, results_dir: str):
        self.results_dir = Path(results_dir)
        self.crashes = []
        self.test_results = []
        self.performance_metrics = []
        self.screenshots = []
        self.videos = []
        
    def parse_all(self) -> Dict:
        """Parse all result files and generate comprehensive report."""
        if not self.results_dir.exists():
            return {"error": f"Results directory {self.results_dir} does not exist"}
        
        self._find_crash_logs()
        self._find_test_results()
        self._find_performance_files()
        self._find_media_files()
        
        return self._generate_report()
    
    def _find_crash_logs(self):
        """Find and parse crash logs."""
        log_patterns = ["*.log", "logcat*", "*test*log*"]
        
        for pattern in log_patterns:
            for log_file in self.results_dir.rglob(pattern):
                if log_file.is_file():
                    try:
                        self._parse_crash_log(log_file)
                    except Exception as e:
                        print(f"Warning: Could not parse {log_file}: {e}")
    
    def _parse_crash_log(self, log_file: Path):
        """Parse individual crash log for FATAL exceptions."""
        try:
            with open(log_file, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()
                
            # Search for FATAL exceptions
            fatal_matches = re.finditer(
                r'FATAL EXCEPTION:.*?(?=\n\n|\Z)',
                content,
                re.MULTILINE | re.DOTALL
            )
            
            for match in fatal_matches:
                crash_info = {
                    'file': str(log_file.relative_to(self.results_dir)),
                    'type': 'FATAL EXCEPTION',
                    'details': match.group().strip(),
                    'timestamp': self._extract_timestamp(content, match.start())
                }
                self.crashes.append(crash_info)
            
            # Search for AndroidRuntime errors
            android_runtime_matches = re.finditer(
                r'AndroidRuntime.*?FATAL.*?(?=\n\n|\Z)',
                content,
                re.MULTILINE | re.DOTALL
            )
            
            for match in android_runtime_matches:
                crash_info = {
                    'file': str(log_file.relative_to(self.results_dir)),
                    'type': 'AndroidRuntime',
                    'details': match.group().strip(),
                    'timestamp': self._extract_timestamp(content, match.start())
                }
                self.crashes.append(crash_info)
                
        except Exception as e:
            print(f"Error parsing {log_file}: {e}")
    
    def _find_test_results(self):
        """Find and parse test result files."""
        result_patterns = ["*.xml", "*.json", "*test*result*"]
        
        for pattern in result_patterns:
            for result_file in self.results_dir.rglob(pattern):
                if result_file.is_file():
                    try:
                        if result_file.suffix == '.xml':
                            self._parse_xml_result(result_file)
                        elif result_file.suffix == '.json':
                            self._parse_json_result(result_file)
                    except Exception as e:
                        print(f"Warning: Could not parse {result_file}: {e}")
    
    def _parse_xml_result(self, xml_file: Path):
        """Parse XML test result files."""
        try:
            tree = ET.parse(xml_file)
            root = tree.getroot()
            
            test_info = {
                'file': str(xml_file.relative_to(self.results_dir)),
                'type': 'XML Test Results',
                'total_tests': len(root.findall('.//testcase')),
                'failures': len(root.findall('.//failure')),
                'errors': len(root.findall('.//error'))
            }
            
            # Extract failure details
            for failure in root.findall('.//failure'):
                failure_info = {
                    'test_name': failure.get('name', 'Unknown'),
                    'message': failure.text.strip()[:200] if failure.text else 'No message',
                    'file': str(xml_file.relative_to(self.results_dir))
                }
                test_info.setdefault('failure_details', []).append(failure_info)
            
            self.test_results.append(test_info)
            
        except Exception as e:
            print(f"Error parsing XML {xml_file}: {e}")
    
    def _parse_json_result(self, json_file: Path):
        """Parse JSON test result files."""
        try:
            with open(json_file, 'r') as f:
                data = json.load(f)
            
            test_info = {
                'file': str(json_file.relative_to(self.results_dir)),
                'type': 'JSON Test Results',
                'data': data
            }
            self.test_results.append(test_info)
            
        except Exception as e:
            print(f"Error parsing JSON {json_file}: {e}")
    
    def _find_performance_files(self):
        """Find performance metric files."""
        perf_patterns = ["*perf*", "*performance*", "metrics*"]
        
        for pattern in perf_patterns:
            for perf_file in self.results_dir.rglob(pattern):
                if perf_file.is_file():
                    self.performance_metrics.append(str(perf_file.relative_to(self.results_dir)))
    
    def _find_media_files(self):
        """Find screenshots and video files."""
        image_patterns = ["*.png", "*.jpg", "*.jpeg"]
        video_patterns = ["*.mp4", "*.avi", "*.mov"]
        
        for pattern in image_patterns + video_patterns:
            for media_file in self.results_dir.rglob(pattern):
                if media_file.is_file():
                    file_info = {
                        'path': str(media_file.relative_to(self.results_dir)),
                        'size_mb': round(media_file.stat().st_size / (1024 * 1024), 2),
                        'type': 'image' if media_file.suffix.lower() in ['.png', '.jpg', '.jpeg'] else 'video'
                    }
                    
                    if file_info['type'] == 'image':
                        self.screenshots.append(file_info)
                    else:
                        self.videos.append(file_info)
    
    def _extract_timestamp(self, content: str, position: int) -> Optional[str]:
        """Extract timestamp near the given position in content."""
        try:
            # Look for timestamp patterns in surrounding text
            start = max(0, position - 200)
            end = min(len(content), position + 200)
            surrounding = content[start:end]
            
            # Common timestamp patterns
            timestamp_patterns = [
                r'\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}',
                r'\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}',
                r'\w{3}\s+\d{1,2}\s+\d{2}:\d{2}:\d{2}'
            ]
            
            for pattern in timestamp_patterns:
                match = re.search(pattern, surrounding)
                if match:
                    return match.group()
            
            return None
        except Exception:
            return None
    
    def _generate_report(self) -> Dict:
        """Generate comprehensive test report."""
        report = {
            'summary': {
                'total_crashes': len(self.crashes),
                'total_test_files': len(self.test_results),
                'total_screenshots': len(self.screenshots),
                'total_videos': len(self.videos),
                'performance_files': len(self.performance_metrics),
                'scan_time': datetime.now().isoformat()
            },
            'crashes': self.crashes,
            'test_results': self.test_results,
            'media_files': {
                'screenshots': self.screenshots,
                'videos': self.videos
            },
            'performance_files': self.performance_metrics
        }
        
        return report

def print_crash_summary(crashes: List[Dict]):
    """Print crash summary to stdout."""
    print("\n" + "="*60)
    print("🚨 FIREBASE TEST LAB - CRASH ANALYSIS")
    print("="*60)
    
    if not crashes:
        print("✅ NO CRASHES DETECTED")
        print("All Firebase Test Lab runs completed without FATAL exceptions.")
        return
    
    print(f"⚠️  CRASHES DETECTED: {len(crashes)} total")
    print()
    
    # Group crashes by type
    crash_types = {}
    for crash in crashes:
        crash_type = crash.get('type', 'Unknown')
        if crash_type not in crash_types:
            crash_types[crash_type] = []
        crash_types[crash_type].append(crash)
    
    for crash_type, type_crashes in crash_types.items():
        print(f"📋 {crash_type}: {len(type_crashes)} incidents")
        print("-" * 40)
        
        for i, crash in enumerate(type_crashes[:3], 1):  # Show first 3 of each type
            print(f"{i}. File: {crash.get('file', 'Unknown')}")
            if crash.get('timestamp'):
                print(f"   Time: {crash['timestamp']}")
            
            details = crash.get('details', '')
            if details:
                # Extract first line for summary
                first_line = details.split('\n')[0]
                print(f"   Summary: {first_line[:100]}...")
            print()

def print_test_summary(test_results: List[Dict]):
    """Print test result summary to stdout."""
    print("\n" + "="*60)
    print("📊 TEST RESULTS SUMMARY")
    print("="*60)
    
    if not test_results:
        print("ℹ️  No test result files found.")
        return
    
    total_files = len(test_results)
    total_failures = sum(result.get('failures', 0) for result in test_results)
    total_errors = sum(result.get('errors', 0) for result in test_results)
    
    print(f"📁 Test Result Files: {total_files}")
    print(f"❌ Total Failures: {total_failures}")
    print(f"🚨 Total Errors: {total_errors}")
    
    # Show first few test files
    for result in test_results[:5]:
        file_name = result.get('file', 'Unknown')
        print(f"  - {file_name}")
        if 'total_tests' in result:
            print(f"    Tests: {result['total_tests']}, Failures: {result['failures']}, Errors: {result['errors']}")

def print_media_summary(screenshots: List[Dict], videos: List[Dict]):
    """Print media files summary to stdout."""
    print("\n" + "="*60)
    print("📸 MEDIA FILES SUMMARY")
    print("="*60)
    
    print(f"🖼️  Screenshots: {len(screenshots)} files")
    for screenshot in screenshots[:3]:  # Show first 3
        print(f"  - {screenshot['path']} ({screenshot['size_mb']} MB)")
    
    print(f"\n🎬 Videos: {len(videos)} files")
    for video in videos[:3]:  # Show first 3
        print(f"  - {video['path']} ({video['size_mb']} MB)")

def main():
    if len(sys.argv) != 2:
        print("Usage: python parse_firebase_results.py <results_directory>")
        sys.exit(1)
    
    results_dir = sys.argv[1]
    
    print("🔍 Firebase Test Lab Results Parser")
    print(f"📁 Scanning directory: {results_dir}")
    
    parser = FirebaseResultParser(results_dir)
    report = parser.parse_all()
    
    if 'error' in report:
        print(f"❌ Error: {report['error']}")
        sys.exit(1)
    
    # Print summaries to stdout (GitHub Actions will capture this)
    print_crash_summary(report['crashes'])
    print_test_summary(report['test_results'])
    print_media_summary(report['media_files']['screenshots'], report['media_files']['videos'])
    
    # Save detailed JSON report
    report_file = Path(results_dir) / "firebase_analysis_report.json"
    try:
        with open(report_file, 'w') as f:
            json.dump(report, f, indent=2)
        print(f"\n📄 Detailed report saved to: {report_file}")
    except Exception as e:
        print(f"⚠️  Could not save report file: {e}")
    
    # Exit with error code if crashes found
    if report['summary']['total_crashes'] > 0:
        print(f"\n❌ Analysis complete: {report['summary']['total_crashes']} crashes detected")
        sys.exit(1)
    else:
        print("\n✅ Analysis complete: No crashes detected")

if __name__ == "__main__":
    main()