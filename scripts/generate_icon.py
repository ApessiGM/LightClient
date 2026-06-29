"""Generates the Light Client icon: a neon-purple glass 'L' on a dark rounded square.

Run with: python3 scripts/generate_icon.py
Outputs to src/main/resources/assets/lightclient/icon.png
"""
import math
import os

from PIL import Image, ImageDraw, ImageFilter

SIZE = 256
OUT = os.path.join(
    os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
    "src", "main", "resources", "assets", "lightclient", "icon.png",
)

NEON = (122, 45, 255)
BRIGHT = (170, 0, 255)
VIOLET = (138, 43, 226)
DARK_A = (18, 18, 18)
DARK_B = (10, 10, 10)


def lerp(a, b, t):
    return tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3))


def rounded_mask(size, radius):
    mask = Image.new("L", (size, size), 0)
    d = ImageDraw.Draw(mask)
    d.rounded_rectangle([0, 0, size - 1, size - 1], radius=radius, fill=255)
    return mask


def main():
    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))

    # Diagonal dark gradient background.
    bg = Image.new("RGBA", (SIZE, SIZE))
    px = bg.load()
    for y in range(SIZE):
        for x in range(SIZE):
            t = (x + y) / (2 * SIZE)
            r, g, b = lerp(DARK_A, DARK_B, t)
            px[x, y] = (r, g, b, 255)

    mask = rounded_mask(SIZE, 56)
    img.paste(bg, (0, 0), mask)

    # Neon border glow.
    border = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    bd = ImageDraw.Draw(border)
    bd.rounded_rectangle([6, 6, SIZE - 7, SIZE - 7], radius=50, outline=BRIGHT + (255,), width=6)
    glow = border.filter(ImageFilter.GaussianBlur(7))
    img = Image.alpha_composite(img, glow)
    img = Image.alpha_composite(img, border)

    # Glass 'L'.
    letter = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    ld = ImageDraw.Draw(letter)
    vx, vy = 86, 56          # vertical stem
    thick = 34
    ld.rounded_rectangle([vx, vy, vx + thick, 196], radius=10, fill=NEON + (255,))
    ld.rounded_rectangle([vx, 196 - thick + 4, 188, 200], radius=10, fill=BRIGHT + (255,))

    lglow = letter.filter(ImageFilter.GaussianBlur(6))
    img = Image.alpha_composite(img, lglow)
    img = Image.alpha_composite(img, letter)

    # Subtle top highlight for the glass effect.
    sheen = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    sd = ImageDraw.Draw(sheen)
    sd.polygon([(20, 20), (150, 20), (60, 130), (20, 130)], fill=(255, 255, 255, 26))
    sheen = Image.composite(sheen, Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0)), mask)
    img = Image.alpha_composite(img, sheen)

    os.makedirs(os.path.dirname(OUT), exist_ok=True)
    img.save(OUT)
    print("wrote", OUT)


if __name__ == "__main__":
    main()
