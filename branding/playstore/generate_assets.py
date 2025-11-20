#!/usr/bin/env python3
"""
AI Keyboard — Play Store Asset Generator
Generates all visual assets: screenshots, feature graphics, icons, and layouts.
"""

import os
import sys
from pathlib import Path
from typing import Tuple, List
from PIL import Image, ImageDraw, ImageFont, ImageFilter
import math

# Brand Colors (from style guide)
ELECTRIC_BLUE = (58, 123, 255)      # #3A7BFF
DEEP_INDIGO = (30, 42, 85)          # #1E2A55
AI_TEAL = (0, 199, 183)             # #00C7B7
SKY_MINT = (160, 255, 233)          # #A0FFE9
MAGENTA_PULSE = (255, 43, 163)      # #FF2BA3
AI_GOLD = (249, 214, 92)            # #F9D65C
PURE_WHITE = (255, 255, 255)
SOFT_GRAY = (232, 234, 240)         # #E8EAF0
DARK_CHARCOAL = (29, 29, 31)        # #1D1D1F

OUTPUT_DIR = Path(__file__).parent / "output"
OUTPUT_DIR.mkdir(exist_ok=True)


def hex_to_rgb(hex_color: str) -> Tuple[int, int, int]:
    """Convert hex color to RGB tuple."""
    hex_color = hex_color.lstrip('#')
    return tuple(int(hex_color[i:i+2], 16) for i in (0, 2, 4))


def create_gradient_background(
    size: Tuple[int, int],
    color1: Tuple[int, int, int],
    color2: Tuple[int, int, int],
    direction: str = "horizontal"
) -> Image.Image:
    """Create a gradient background."""
    width, height = size
    img = Image.new('RGB', size)
    pixels = img.load()
    
    if direction == "horizontal":
        for x in range(width):
            ratio = x / width
            r = int(color1[0] * (1 - ratio) + color2[0] * ratio)
            g = int(color1[1] * (1 - ratio) + color2[1] * ratio)
            b = int(color1[2] * (1 - ratio) + color2[2] * ratio)
            for y in range(height):
                pixels[x, y] = (r, g, b)
    elif direction == "vertical":
        for y in range(height):
            ratio = y / height
            r = int(color1[0] * (1 - ratio) + color2[0] * ratio)
            g = int(color1[1] * (1 - ratio) + color2[1] * ratio)
            b = int(color1[2] * (1 - ratio) + color2[2] * ratio)
            for x in range(width):
                pixels[x, y] = (r, g, b)
    else:  # diagonal
        max_dist = math.sqrt(width**2 + height**2)
        for x in range(width):
            for y in range(height):
                # Distance from top-left
                dist = math.sqrt(x**2 + y**2) / max_dist
                ratio = min(dist, 1.0)
                r = int(color1[0] * (1 - ratio) + color2[0] * ratio)
                g = int(color1[1] * (1 - ratio) + color2[1] * ratio)
                b = int(color1[2] * (1 - ratio) + color2[2] * ratio)
                pixels[x, y] = (r, g, b)
    
    return img


def get_font(size: int, bold: bool = False) -> ImageFont.FreeTypeFont:
    """Get font, falling back to default if Inter not available."""
    script_dir = Path(__file__).parent
    font_paths = [
        str(script_dir / "fonts" / "Inter-Bold.ttf"),
        str(script_dir / "fonts" / "Inter-SemiBold.ttf"),
        str(script_dir / "fonts" / "Inter-Regular.ttf"),
        "/usr/share/fonts/truetype/inter/Inter-Bold.ttf",
        "/usr/share/fonts/truetype/inter/Inter-SemiBold.ttf",
        "/System/Library/Fonts/Helvetica.ttc",
        "C:/Windows/Fonts/arialbd.ttf",
        "C:/Windows/Fonts/arial.ttf",
    ]
    
    if bold:
        # Try bold fonts first
        bold_fonts = [fp for fp in font_paths if "Bold" in fp or "bd" in fp.lower()]
        font_paths = bold_fonts + [fp for fp in font_paths if fp not in bold_fonts]
    
    for font_path in font_paths:
        if os.path.exists(font_path):
            try:
                return ImageFont.truetype(font_path, size)
            except:
                continue
    
    # Fallback to default font
    try:
        if sys.platform == "win32":
            fallback = "arialbd.ttf" if bold else "arial.ttf"
            return ImageFont.truetype(f"C:/Windows/Fonts/{fallback}", size)
        else:
            return ImageFont.load_default()
    except:
        return ImageFont.load_default()


def draw_text_with_shadow(
    draw: ImageDraw.Draw,
    text: str,
    position: Tuple[int, int],
    font: ImageFont.FreeTypeFont,
    fill: Tuple[int, int, int],
    shadow_offset: int = 3
) -> None:
    """Draw text with shadow for better readability."""
    x, y = position
    # Draw shadow
    draw.text((x + shadow_offset, y + shadow_offset), text, font=font, fill=(0, 0, 0, 128))
    # Draw main text
    draw.text((x, y), text, font=font, fill=fill)


def draw_waveform(
    draw: ImageDraw.Draw,
    x: int,
    y: int,
    width: int,
    height: int,
    amplitude: float = 1.0,
    color: Tuple[int, int, int] = AI_TEAL
) -> None:
    """Draw a waveform pattern."""
    points = []
    num_points = 100
    center_y = y + height // 2
    
    for i in range(num_points):
        px = x + (i / num_points) * width
        py = center_y + math.sin(i * 0.3) * (height / 2) * amplitude
        points.append((px, py))
    
    if len(points) > 1:
        draw.line(points, fill=color, width=4)


def draw_keyboard_mockup(
    draw: ImageDraw.Draw,
    x: int,
    y: int,
    width: int,
    height: int,
    key_color: Tuple[int, int, int] = (200, 200, 200)
) -> None:
    """Draw a simple keyboard mockup."""
    key_width = width // 10
    key_height = height // 4
    key_spacing = 5
    corner_radius = 8
    
    # Top row (QWERTY)
    for i, letter in enumerate("QWERTYUIOP"):
        key_x = x + i * (key_width + key_spacing)
        key_y = y
        
        # Draw rounded rectangle key
        draw.rounded_rectangle(
            [key_x, key_y, key_x + key_width, key_y + key_height],
            radius=corner_radius,
            fill=key_color,
            outline=(150, 150, 150),
            width=2
        )
        
        # Draw letter
        font = get_font(16, bold=True)
        bbox = draw.textbbox((0, 0), letter, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]
        draw.text(
            (key_x + (key_width - text_width) // 2, key_y + (key_height - text_height) // 2),
            letter,
            font=font,
            fill=DEEP_INDIGO
        )


def generate_feature_graphic() -> Image.Image:
    """Generate Play Store feature graphic (1024 × 500)."""
    width, height = 1024, 500
    img = create_gradient_background((width, height), ELECTRIC_BLUE, AI_TEAL, "horizontal")
    draw = ImageDraw.Draw(img)
    
    # Title
    title_font = get_font(72, bold=True)
    title = "AI Keyboard"
    title_bbox = draw.textbbox((0, 0), title, font=title_font)
    title_width = title_bbox[2] - title_bbox[0]
    title_x = (width - title_width) // 2
    title_y = 100
    draw_text_with_shadow(draw, title, (title_x, title_y), title_font, PURE_WHITE)
    
    # Subtitle
    subtitle_font = get_font(32, bold=False)
    subtitle = "Offline AI Voice Typing with Your Own Models"
    subtitle_bbox = draw.textbbox((0, 0), subtitle, font=subtitle_font)
    subtitle_width = subtitle_bbox[2] - subtitle_bbox[0]
    subtitle_x = (width - subtitle_width) // 2
    subtitle_y = title_y + 80
    draw_text_with_shadow(draw, subtitle, (subtitle_x, subtitle_y), subtitle_font, SKY_MINT)
    
    # Keyboard mockup (bottom center)
    keyboard_x = width // 2 - 200
    keyboard_y = height - 120
    draw_keyboard_mockup(draw, keyboard_x, keyboard_y, 400, 80, PURE_WHITE)
    
    # Mic icon (circle with waveform)
    mic_x, mic_y = keyboard_x - 100, keyboard_y + 40
    mic_radius = 30
    draw.ellipse(
        [mic_x - mic_radius, mic_y - mic_radius, mic_x + mic_radius, mic_y + mic_radius],
        fill=MAGENTA_PULSE,
        outline=PURE_WHITE,
        width=3
    )
    
    # Waveform near mic
    draw_waveform(draw, mic_x + mic_radius + 20, mic_y - 20, 150, 40, 0.8, PURE_WHITE)
    
    return img


def draw_ai_logo_simple(draw: ImageDraw.Draw, center_x: int, center_y: int, size: int) -> None:
    """Draw a simple AI monogram logo."""
    # Draw rounded square background
    half_size = size // 2
    corner_radius = size // 8
    
    # Create gradient effect with multiple rectangles
    draw.rounded_rectangle(
        [center_x - half_size, center_y - half_size, 
         center_x + half_size, center_y + half_size],
        radius=corner_radius,
        fill=ELECTRIC_BLUE
    )
    
    # Draw "AI" monogram
    letter_size = size // 3
    font = get_font(letter_size, bold=True)
    
    # Calculate text dimensions for centering
    text = "AI"
    bbox = draw.textbbox((0, 0), text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    text_x = center_x - text_width // 2
    text_y = center_y - text_height // 2 - letter_size // 8
    
    draw.text((text_x, text_y), text, font=font, fill=PURE_WHITE)
    
    # Draw waveform accent below
    wave_x = center_x - letter_size // 2
    wave_y = center_y + letter_size // 4
    draw_waveform(draw, wave_x, wave_y, letter_size, letter_size // 6, 0.6, SKY_MINT)


def generate_app_icon() -> Image.Image:
    """Generate app icon (512 × 512)."""
    size = 512
    # Background with gradient
    img = create_gradient_background((size, size), DEEP_INDIGO, ELECTRIC_BLUE, "diagonal")
    draw = ImageDraw.Draw(img)
    
    # Draw AI logo in center
    draw_ai_logo_simple(draw, size // 2, size // 2, size // 2)
    
    # Add subtle mic waveform at bottom
    draw_waveform(draw, size // 4, size * 3 // 4, size // 2, 40, 0.5, AI_TEAL)
    
    return img


def draw_device_frame(
    draw: ImageDraw.Draw,
    x: int,
    y: int,
    width: int,
    height: int,
    color: Tuple[int, int, int] = (50, 50, 50)
) -> None:
    """Draw a simple device frame."""
    # Outer frame
    border_width = 20
    draw.rectangle(
        [x, y, x + width, y + height],
        outline=color,
        width=border_width
    )
    
    # Notch/screen indicator at top
    notch_width = 100
    notch_height = 20
    draw.rectangle(
        [x + (width - notch_width) // 2, y, 
         x + (width + notch_width) // 2, y + notch_height],
        fill=color
    )


def generate_screenshot(
    title: str,
    subtitle: str,
    screenshot_number: int,
    accent_color: Tuple[int, int, int] = AI_TEAL
) -> Image.Image:
    """Generate a screenshot template (1080 × 1920)."""
    width, height = 1080, 1920
    img = create_gradient_background((width, height), DEEP_INDIGO, ELECTRIC_BLUE, "vertical")
    draw = ImageDraw.Draw(img)
    
    # Device frame area (centered)
    device_width = width - 200
    device_height = int(device_width * 16 / 9)  # 16:9 aspect ratio
    device_x = (width - device_width) // 2
    device_y = 300
    
    # Draw device frame
    draw_device_frame(draw, device_x, device_y, device_width, device_height, (40, 40, 40))
    
    # Inner screen area (with gradient background)
    screen_margin = 30
    screen_x = device_x + screen_margin
    screen_y = device_y + screen_margin
    screen_width = device_width - 2 * screen_margin
    screen_height = device_height - 2 * screen_margin
    
    screen_bg = create_gradient_background(
        (screen_width, screen_height),
        DARK_CHARCOAL,
        (40, 45, 60)
    )
    img.paste(screen_bg, (screen_x, screen_y))
    
    # Draw simple UI elements based on screenshot number
    if screenshot_number == 1:
        # Model selection mockup
        draw_keyboard_mockup(draw, screen_x + 100, screen_y + 200, 300, 100, (60, 60, 70))
    elif screenshot_number == 2:
        # Waveform display
        draw_waveform(draw, screen_x + 100, screen_y + 400, 700, 200, 1.0, AI_TEAL)
        draw.ellipse(
            [screen_x + 400, screen_y + 800, screen_x + 500, screen_y + 900],
            fill=MAGENTA_PULSE,
            outline=PURE_WHITE,
            width=5
        )
    elif screenshot_number == 3:
        # Model cards mockup
        card_y = screen_y + 200
        for i in range(2):
            draw.rounded_rectangle(
                [screen_x + 50, card_y + i * 200, screen_x + screen_width - 50, card_y + 150 + i * 200],
                radius=12,
                fill=(60, 60, 70),
                outline=AI_TEAL if i == 0 else (100, 100, 100),
                width=3
            )
    elif screenshot_number == 4:
        # Theme selector mockup
        draw.rounded_rectangle(
            [screen_x + 200, screen_y + 300, screen_x + 600, screen_y + 800],
            radius=20,
            fill=(60, 60, 70),
            outline=ELECTRIC_BLUE,
            width=4
        )
    elif screenshot_number == 5:
        # Keyboard with swipe trail
        draw_keyboard_mockup(draw, screen_x + 100, screen_y + 500, 600, 200, (60, 60, 70))
        # Draw swipe trail curve
        trail_points = [(200, 600), (300, 550), (400, 580), (500, 560), (600, 590)]
        adjusted_points = [(screen_x + p[0], screen_y + p[1]) for p in trail_points]
        draw.line(adjusted_points, fill=AI_TEAL, width=6)
    elif screenshot_number == 6:
        # Clipboard and emoji mockup
        draw.rounded_rectangle(
            [screen_x + 100, screen_y + 300, screen_x + screen_width - 100, screen_y + 500],
            radius=12,
            fill=(60, 60, 70),
            outline=SKY_MINT,
            width=2
        )
        draw_keyboard_mockup(draw, screen_x + 100, screen_y + 600, 600, 150, (60, 60, 70))
    elif screenshot_number == 7:
        # Support/coffee mockup
        draw.ellipse(
            [screen_x + 300, screen_y + 400, screen_x + 600, screen_y + 700],
            fill=AI_GOLD,
            outline=PURE_WHITE,
            width=5
        )
        font = get_font(80, bold=True)
        # Calculate text dimensions for centering
        emoji_bbox = draw.textbbox((0, 0), "☕", font=font)
        emoji_width = emoji_bbox[2] - emoji_bbox[0]
        emoji_height = emoji_bbox[3] - emoji_bbox[1]
        emoji_x = (screen_x + 450) - emoji_width // 2
        emoji_y = (screen_y + 550) - emoji_height // 2
        draw.text((emoji_x, emoji_y), "☕", font=font, fill=PURE_WHITE)
    
    # Title text (top overlay)
    title_font = get_font(64, bold=True)
    title_bbox = draw.textbbox((0, 0), title, font=title_font)
    title_width = title_bbox[2] - title_bbox[0]
    title_x = (width - title_width) // 2
    title_y = 80
    draw_text_with_shadow(draw, title, (title_x, title_y), title_font, PURE_WHITE, shadow_offset=4)
    
    # Subtitle text
    subtitle_font = get_font(36, bold=False)
    subtitle_bbox = draw.textbbox((0, 0), subtitle, font=subtitle_font)
    subtitle_width = subtitle_bbox[2] - subtitle_bbox[0]
    subtitle_x = (width - subtitle_width) // 2
    subtitle_y = title_y + 80
    draw_text_with_shadow(draw, subtitle, (subtitle_x, subtitle_y), subtitle_font, accent_color, shadow_offset=3)
    
    return img


def generate_all_assets():
    """Generate all Play Store assets."""
    print("🎨 Generating Play Store assets...")
    
    # 1. Feature Graphic
    print("  📐 Generating feature graphic (1024×500)...")
    feature_graphic = generate_feature_graphic()
    feature_graphic.save(OUTPUT_DIR / "feature-graphic.png", "PNG")
    print(f"     ✅ Saved: {OUTPUT_DIR / 'feature-graphic.png'}")
    
    # 2. App Icon
    print("  🎯 Generating app icon (512×512)...")
    app_icon = generate_app_icon()
    app_icon.save(OUTPUT_DIR / "app-icon-512.png", "PNG")
    print(f"     ✅ Saved: {OUTPUT_DIR / 'app-icon-512.png'}")
    
    # Generate adaptive icon sizes
    sizes = [
        (48, "mdpi"),
        (72, "hdpi"),
        (96, "xhdpi"),
        (144, "xxhdpi"),
        (192, "xxxhdpi"),
    ]
    
    for size, density in sizes:
        icon_resized = app_icon.resize((size, size), Image.Resampling.LANCZOS)
        icon_resized.save(OUTPUT_DIR / f"app-icon-{density}-{size}x{size}.png", "PNG")
        print(f"     ✅ Saved: {OUTPUT_DIR / f'app-icon-{density}-{size}x{size}.png'}")
    
    # 3. Screenshots
    screenshots = [
        ("Offline AI Voice Typing", "Choose your own AI model", 1, AI_TEAL),
        ("Real-time Transcription", "Fast, accurate, private", 2, AI_TEAL),
        ("Install Any ONNX Model", "Parakeet, Distil-Whisper, Vosk & more", 3, SKY_MINT),
        ("Beautiful Themes", "Customize colors, shapes, and layout", 4, ELECTRIC_BLUE),
        ("Smart Typing", "AI-powered predictions and swipe typing", 5, AI_TEAL),
        ("Clipboard & Emoji", "Everything you need in one keyboard", 6, SKY_MINT),
        ("Support the Project", "Buy me a coffee ❤️", 7, AI_GOLD),
    ]
    
    print("  📱 Generating screenshots (1080×1920)...")
    for i, (title, subtitle, num, accent) in enumerate(screenshots, 1):
        screenshot = generate_screenshot(title, subtitle, num, accent)
        screenshot.save(OUTPUT_DIR / f"screenshot-{i:02d}-{title.lower().replace(' ', '-')}.png", "PNG")
        print(f"     ✅ Saved: {OUTPUT_DIR / f'screenshot-{i:02d}-{title.lower().replace(' ', '-')}.png'}")
    
    print(f"\n✨ All assets generated successfully in: {OUTPUT_DIR}")
    print(f"   Total files: {len(list(OUTPUT_DIR.glob('*.png')))}")


if __name__ == "__main__":
    try:
        generate_all_assets()
    except Exception as e:
        print(f"❌ Error generating assets: {e}", file=sys.stderr)
        import traceback
        traceback.print_exc()
        sys.exit(1)

