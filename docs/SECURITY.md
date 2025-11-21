# Security Policy

## Supported Versions

We actively support the following versions of AI Keyboard:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.0+  | :white_check_mark: |
| < 1.0.0 | :x:                |

---

## Security Overview

AI Keyboard is built with security as a core principle. This document outlines our security practices, threat model, and vulnerability reporting process.

---

## Security Practices

### Model Verification

Models are verified using SHA-256 checksums during installation:

- **Checksum Validation**: Models are validated against SHA-256 checksums when provided
- **Integrity Check**: Ensures model files are not corrupted or tampered with
- **User Control**: Users can verify checksums before installation

### Secure Storage

- **App-Private Directory**: All data is stored in the app's private directory
- **Access Control**: Other apps cannot access AI Keyboard data
- **Model Storage**: Models are stored securely in app-private storage

### Secure Downloads

- **HTTPS Only**: Model downloads use secure connections (HTTPS)
- **User Consent**: Models are only downloaded with explicit user consent
- **URL Validation**: Model download URLs are validated before download

### Permissions

AI Keyboard requests minimal permissions:

- **RECORD_AUDIO**: Only used during voice input sessions
- **INTERNET**: Only used when downloading models (user-initiated)
- **ACCESS_NETWORK_STATE**: Only used to check network availability
- **READ_EXTERNAL_STORAGE**: Only used when installing models from files

---

## Threat Model

### Potential Threats

#### 1. Model Tampering

**Threat**: Malicious model files could compromise the device or steal data.

**Mitigation**:
- SHA-256 checksum verification
- User control over model installation
- App-private storage for models
- No automatic model installation

#### 2. Data Theft

**Threat**: Personal data (dictionary, clipboard) could be stolen.

**Mitigation**:
- App-private storage
- No cloud sync (unless user enables)
- No network calls (except user-initiated downloads)
- Local-only data storage

#### 3. Voice Data Interception

**Threat**: Voice data could be intercepted or transmitted to external servers.

**Mitigation**:
- 100% offline processing
- No network calls during voice input
- No audio data storage
- Microphone only active during voice input sessions

#### 4. Model Security Issues

**Threat**: Models could contain malicious code or vulnerabilities.

**Mitigation**:
- User control over model installation
- Checksum verification
- No automatic model execution
- App-private storage isolation

---

## Safe On-Device Processing

### Offline Processing

- **100% Offline**: All voice recognition happens on-device
- **No Cloud Processing**: No data is sent to cloud servers
- **No Network Calls**: No network activity during voice input
- **Local Only**: All processing happens locally

### Data Isolation

- **App-Private Storage**: All data is stored in app-private directory
- **Process Isolation**: AI Keyboard runs in its own process
- **No Cross-App Access**: Other apps cannot access AI Keyboard data
- **User Control**: Users control data deletion and management

---

## Permissions Used

### RECORD_AUDIO

- **Purpose**: Voice input functionality
- **When Used**: Only during voice input sessions
- **Data**: Audio is processed locally, not stored
- **Network**: No audio data is transmitted

### INTERNET

- **Purpose**: Download models from URL
- **When Used**: Only when user downloads models
- **Data**: Only model files are downloaded
- **Privacy**: No personal data is transmitted

### ACCESS_NETWORK_STATE

- **Purpose**: Check network availability before downloads
- **When Used**: Before model downloads
- **Data**: No data is transmitted
- **Privacy**: No personal data is accessed

### READ_EXTERNAL_STORAGE / READ_MEDIA_AUDIO

- **Purpose**: Install models from local files
- **When Used**: Only when user installs models from files
- **Data**: Only model files are accessed
- **Privacy**: No other files are accessed

---

## Vulnerability Reporting

### How to Report

If you discover a security vulnerability, please report it responsibly:

1. **Do NOT** create a public GitHub issue
2. **Email** security details to: *(Security email will be added)*
3. **Include**:
   - Description of the vulnerability
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if any)

### Reporting Process

1. **Report**: Send security details via email
2. **Acknowledge**: We will acknowledge receipt within 48 hours
3. **Investigate**: We will investigate and respond within 7 days
4. **Fix**: We will fix the vulnerability and release an update
5. **Disclose**: We will disclose the vulnerability after the fix is released

### Vulnerability Disclosure

- **Responsible Disclosure**: We follow responsible disclosure practices
- **Fix First**: Vulnerabilities are fixed before public disclosure
- **Security Advisories**: Security advisories are published after fixes
- **Credit**: Security researchers are credited (if desired)

---

## Security Best Practices

### For Users

1. **Download Models from Trusted Sources**: Only download models from official repositories
2. **Verify Checksums**: Verify model checksums when provided
3. **Keep App Updated**: Keep AI Keyboard updated to the latest version
4. **Review Permissions**: Review and manage app permissions regularly
5. **Report Issues**: Report security issues responsibly

### For Developers

1. **Follow Security Guidelines**: Follow Android security best practices
2. **Code Review**: Review code for security issues
3. **Dependency Updates**: Keep dependencies updated
4. **Testing**: Test for security vulnerabilities
5. **Documentation**: Document security practices

---

## Security Checklist

### Pre-Release Checklist

- [ ] No hardcoded credentials or API keys
- [ ] Secure storage for sensitive data
- [ ] Proper permission handling
- [ ] Input validation for all user inputs
- [ ] Model verification implemented
- [ ] Secure network communication (HTTPS)
- [ ] No sensitive data in logs
- [ ] ProGuard/R8 enabled for release builds

### Ongoing Security

- [ ] Regular dependency updates
- [ ] Security vulnerability scanning
- [ ] Code review for security issues
- [ ] Security testing
- [ ] Incident response plan

---

## Incident Response

### Security Incident Process

1. **Identify**: Identify and confirm the security incident
2. **Contain**: Contain the incident to prevent further damage
3. **Investigate**: Investigate the cause and impact
4. **Remediate**: Fix the vulnerability or issue
5. **Notify**: Notify affected users (if applicable)
6. **Document**: Document the incident and response
7. **Review**: Review and improve security practices

### Contact for Security Issues

- **Email**: *(Security email will be added)*
- **GitHub Issues**: For non-sensitive security issues
- **X/Twitter**: [@MjYoke1111](https://x.com/MjYoke1111) (for public communication)

---

## Security Resources

### Android Security

- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [Android Security Guidelines](https://developer.android.com/training/articles/security-tips)
- [Android Security Bulletins](https://source.android.com/security/bulletin)

### Security Tools

- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)
- [Android Security Testing](https://developer.android.com/training/articles/security-tips)

---

## Security Updates

We will update this security policy as needed. Check this page regularly for updates.

---

**Last Updated**: November 2024

**AI Keyboard** — Secure. Private. Offline.












