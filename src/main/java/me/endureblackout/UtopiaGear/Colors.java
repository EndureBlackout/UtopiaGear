package me.endureblackout.UtopiaGear;

import org.bukkit.Color;

public class Colors {

	public String color = "";

	public Colors(String color) {
		this.color = color;
	}

	public Color getColor() {
		if (color.equalsIgnoreCase("Aqua")) {
			return Color.AQUA;
		} else if (color.equalsIgnoreCase("Black")) {
			return Color.BLACK;
		} else if (color.equalsIgnoreCase("Blue")) {
			return Color.BLUE;
		} else if (color.equalsIgnoreCase("FUCHSIA")) {
			return Color.FUCHSIA;
		} else if (color.equalsIgnoreCase("Gray")) {
			return Color.GRAY;
		} else if (color.equalsIgnoreCase("Green")) {
			return Color.GREEN;
		} else if (color.equalsIgnoreCase("lime")) {
			return Color.LIME;
		} else if (color.equalsIgnoreCase("Maroon")) {
			return Color.MAROON;
		} else if (color.equalsIgnoreCase("Navy")) {
			return Color.NAVY;
		} else if (color.equalsIgnoreCase("Olive")) {
			return Color.OLIVE;
		} else if (color.equalsIgnoreCase("Orange")) {
			return Color.ORANGE;
		} else if (color.equalsIgnoreCase("Purple")) {
			return Color.PURPLE;
		} else if (color.equalsIgnoreCase("red")) {
			return Color.RED;
		} else if (color.equalsIgnoreCase("silver")) {
			return Color.SILVER;
		} else if (color.equalsIgnoreCase("teal")) {
			return Color.TEAL;
		} else if (color.equalsIgnoreCase("white")) {
			return Color.WHITE;
		} else if (color.equalsIgnoreCase("yellow")) {
			return Color.YELLOW;
		} else if (color.equalsIgnoreCase("pink")) {
			return Color.fromRGB(255, 192, 203);
		} else if (color.equalsIgnoreCase("light blue")) {
			return Color.fromRGB(30, 144, 255);
		}

		return null;
	}
}
