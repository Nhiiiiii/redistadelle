package domain;


public class District {

    String name;
    Integer value;
    Type type;
    String description;
    Bonus bonus;
    
    enum Type {
        LORDLY,
        SACRED,
        SHOP,
        MILITARY,
        WONDER;
    }
    
    enum Bonus {
        // TODO
    }
    
    Integer getBonus(Player player) {
        // TODO
        return 0;
    }
}
