# Asset Generator Usage Guide

## Quick Start

1. **Install dependencies**:
   ```bash
   pip install -r requirements.txt
   ```

2. **Generate assets**:
   ```bash
   python generate_assets.py
   ```

3. **Find outputs**:
   All generated assets are saved to `branding/playstore/output/`

## Font Setup (Optional)

For best results, download Inter font from [Google Fonts](https://fonts.google.com/specimen/Inter) and extract to:

```
branding/playstore/fonts/
├── Inter-Bold.ttf
├── Inter-SemiBold.ttf
└── Inter-Regular.ttf
```

The script will automatically detect and use Inter if available, otherwise it will fall back to system fonts (Arial on Windows, default on Linux/macOS).

## Customization

### Changing Colors

Edit color constants in `generate_assets.py`:

```python
ELECTRIC_BLUE = (58, 123, 255)      # #3A7BFF
AI_TEAL = (0, 199, 183)             # #00C7B7
# ... etc
```

### Changing Text

Edit text in `generate_all_assets()` function:

```python
screenshots = [
    ("Your Title", "Your Subtitle", 1, AI_TEAL),
    # ... more screenshots
]
```

### Changing Sizes

All sizes are defined at the top of each generator function. Modify as needed:

```python
def generate_feature_graphic() -> Image.Image:
    width, height = 1024, 500  # Change here
    # ...
```

## Output Files

After running, you'll get:

- `feature-graphic.png` - 1024×500
- `app-icon-512.png` - 512×512
- `app-icon-*-*.png` - Various adaptive sizes
- `screenshot-*.png` - 7 screenshots, 1080×1920 each

## Tips

1. **High Quality**: All images are exported as PNG with maximum quality
2. **Optimization**: Use `pngquant` or `optipng` for web optimization if needed
3. **Format Conversion**: Use ImageMagick or PIL to convert to JPEG if required
4. **Batch Processing**: Modify `generate_all_assets()` to process multiple variations

## Troubleshooting

### ImportError: No module named 'PIL'

```bash
pip install --upgrade Pillow
```

### Font not found

The script will use system fonts as fallback. For Inter, download and place in `fonts/` directory.

### Image quality issues

Ensure Pillow is up to date:
```bash
pip install --upgrade Pillow
```

### Text not rendering

Check font paths in `get_font()` function and ensure font files exist.

---

For more details, see [README.md](README.md)

