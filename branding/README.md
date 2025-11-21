# AI Keyboard — Branding Pack

Complete branding assets and guidelines for AI Keyboard Android application.

---

## 📁 Directory Structure

```
branding/
├── logo/
│   ├── svg/                    # Logo SVG files (all concepts & variants)
│   │   ├── concept-a-*.svg     # AI Wave Key (3 variants)
│   │   ├── concept-b-*.svg     # Spark Key (3 variants)
│   │   ├── concept-c-*.svg     # Neural Keyboard (3 variants)
│   │   └── concept-d-*.svg     # Mic + Key Hybrid (3 variants)
│   └── png/                    # PNG exports (to be generated)
│
├── adaptive-icons/             # Android adaptive icon XMLs
│   ├── ic_launcher_*.xml       # Alternative icon concepts
│   └── ic_launcher_foreground_*.xml
│
├── colors/                     # Color palette exports
│   └── color-palette.pdf       # (Optional: color swatch sheet)
│
├── typography/                 # Typography specifications
│   └── typography-spec.pdf     # (Optional: type scale diagram)
│
├── style-guide/                # UI style guide documentation
│   └── UI_STYLE_GUIDE.md       # Complete style guide
│
└── screenshots/                # Mockup documentation
    └── MOCKUP_SCREENSHOTS.md   # Screen mockup descriptions
```

---

## 🎨 Logo Concepts

Four logo concepts are provided, each in three variants:

### Concept A: AI Wave Key ⭐ **RECOMMENDED**
- **Files**: `logo/svg/concept-a-ai-wave-key-*.svg`
- **Description**: Keyboard key with "AI" monogram formed by wave/speech pattern
- **Best for**: Main app icon, marketing materials
- **Variants**: Solid, Outline, Gradient

### Concept B: Spark Key
- **Files**: `logo/svg/concept-b-spark-key-*.svg`
- **Description**: Keycap with spark/star at corner
- **Best for**: Minimal, app-store friendly icon
- **Variants**: Solid, Outline, Gradient

### Concept C: Neural Keyboard
- **Files**: `logo/svg/concept-c-neural-keyboard-*.svg`
- **Description**: Keyboard key with neural network nodes
- **Best for**: Tech-forward, developer-focused materials
- **Variants**: Solid, Outline, Gradient

### Concept D: Mic + Key Hybrid
- **Files**: `logo/svg/concept-d-mic-key-hybrid-*.svg`
- **Description**: Microphone merging into keycap
- **Best for**: Emphasizing voice input feature
- **Variants**: Solid, Outline, Gradient

---

## 🎨 Color Palette

### Primary Colors
- **Electric Blue**: `#3A7BFF` (Primary actions, app icon)
- **Deep Indigo**: `#1E2A55` (Dark theme, on-primary text)

### Secondary Colors
- **AI Teal**: `#00C7B7` (Secondary actions, accents)
- **Sky Mint**: `#A0FFE9` (Primary container, highlights)

### Neutrals
- **Pure White**: `#FFFFFF` (Light backgrounds)
- **Soft Gray**: `#E8EAF0` (Surface variants)
- **Dark Charcoal**: `#1D1D1F` (Dark backgrounds)

### Accents
- **Magenta Pulse**: `#FF2BA3` (Recording indicator, highlights)
- **AI Gold**: `#F9D65C` (Premium features, Pro badge)

**Full color specifications**: See `style-guide/UI_STYLE_GUIDE.md`

---

## 📝 Typography

### Primary Font: Inter
- **Headings**: Inter SemiBold / Bold
- **Body**: Inter Regular
- **Download**: https://fonts.google.com/specimen/Inter

### Code Font: JetBrains Mono
- **Usage**: Debug screens, code snippets, metrics
- **Download**: https://www.jetbrains.com/lp/mono/

**Full typography system**: See `style-guide/UI_STYLE_GUIDE.md`

---

## 📱 Adaptive Icons

Android 13+ adaptive icons are located in:
- **Main app**: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` (Concept A)
- **Alternatives**: `branding/adaptive-icons/` (Concepts B, C, D)

To switch icon concepts, replace the foreground drawable in `ic_launcher.xml`.

---

## 📖 Documentation

### Style Guide
**Location**: `branding/style-guide/UI_STYLE_GUIDE.md`

Complete UI style guide including:
- Color system
- Typography scale
- Logo usage
- UI component specifications
- Animation guidelines
- Accessibility requirements
- Implementation examples

### Mockup Documentation
**Location**: `branding/screenshots/MOCKUP_SCREENSHOTS.md`

Detailed descriptions of:
- Light theme keyboard
- Dark theme keyboard
- Voice input recording state
- Model selection screen
- Settings screen
- Animation sequences

---

## 🚀 Quick Start

### 1. Choose a Logo Concept
Review all four concepts in `logo/svg/` and select one for the app.

### 2. Update App Icon
```bash
# Copy selected adaptive icon
cp branding/adaptive-icons/ic_launcher_[concept].xml \
   app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml
```

### 3. Verify Colors
Colors are already implemented in:
- `app/src/main/res/values/colors.xml` (XML resources)
- `app/src/main/java/com/aikeyboard/ui/theme/Theme.kt` (Compose theme)

### 4. Add Fonts (Optional)
1. Download Inter and JetBrains Mono
2. Place font files in `app/src/main/res/font/`
3. Update `Type.kt` to reference font resources

### 5. Review Style Guide
Read `branding/style-guide/UI_STYLE_GUIDE.md` for detailed implementation guidelines.

---

## 📐 Usage Guidelines

### Logo Usage
- **Minimum Size**: 24dp × 24dp (Android)
- **Safe Zone**: Keep 10% margin from edges
- **Do Not**: Stretch, skew, or rotate logos
- **Do Not**: Apply purple/blue gradients (per brand rules)

### Color Usage
- **Primary**: Use Electric Blue for main actions
- **Secondary**: Use AI Teal for accents
- **Recording**: Use Magenta Pulse for recording states
- **Premium**: Use AI Gold for Pro/premium features

### Typography
- **Headings**: Use Inter SemiBold or Bold
- **Body**: Use Inter Regular
- **Code**: Use JetBrains Mono for technical content

---

## ✅ Brand Compliance Checklist

- [ ] Logo used consistently across all screens
- [ ] No purple/blue color combinations
- [ ] Colors match brand palette
- [ ] Typography uses Inter (or system fallback)
- [ ] Animations are subtle (150-300ms)
- [ ] Icons follow Material 3 guidelines
- [ ] Dark mode properly supported
- [ ] Accessibility features implemented

---

## 📦 Export Requirements

### Play Store Assets
- **App Icon**: 512 × 512px (PNG)
- **Feature Graphic**: 1024 × 500px (PNG)
- **Screenshots**: 
  - Phone: 1080 × 1920px or larger (at least 320dpi)
  - Tablet: 2048 × 2732px (if applicable)

### Marketing Materials
- **Logo**: SVG (vector) preferred, PNG fallback
- **Colors**: Provide hex codes and RGB values
- **Fonts**: Provide font files or links to downloads

---

## 🔗 Resources

- **Inter Font**: https://fonts.google.com/specimen/Inter
- **JetBrains Mono**: https://www.jetbrains.com/lp/mono/
- **Material 3 Design**: https://m3.material.io/
- **Android Adaptive Icons**: https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive

---

## 📝 Notes

- All logos are provided as SVG for scalability
- PNG exports can be generated from SVGs at any resolution
- Color values are provided in hex format
- Typography system follows Material 3 specifications
- All assets are optimized for Android development

---

**Questions?** Refer to the style guide or contact the project maintainer.












