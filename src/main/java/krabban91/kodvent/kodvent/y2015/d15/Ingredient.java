package krabban91.kodvent.kodvent.y2015.d15;

public class Ingredient {
    private final String name;
    private final int capacity;
    private final int durability;
    private final int flavor;
    private final int texture;
    private final int calories;

    public Ingredient(String instructions) {
        String[] s = instructions.split(" ");
        name = s[0].replace(":", "");
        capacity = Integer.parseInt(s[2].replace(",", ""));
        durability = Integer.parseInt(s[4].replace(",", ""));
        flavor = Integer.parseInt(s[6].replace(",", ""));
        texture = Integer.parseInt(s[8].replace(",", ""));
        calories = Integer.parseInt(s[10]);
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDurability() {
        return durability;
    }

    public int getFlavor() {
        return flavor;
    }

    public int getTexture() {
        return texture;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
