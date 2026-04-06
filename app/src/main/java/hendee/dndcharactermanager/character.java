package hendee.dndcharactermanager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Greg on 9/20/2017.
 */

public class character implements Serializable {
    public String name;
    public String gender;
    public String race;
    public int copper;
    public int Silver;
    public int electrum;
    public int gold;
    public int platinum;
    public List<String> languages;
    public List<String> traits;
    public List<String> skillProficiencies;
    public List<String> toolProficiencies;
    public List<String> armorProficiencies;
    public List<String> weaponProficiencies;
    public String alignment;
    public String diety;
    public String background;
    public String characterClass;
    public int strAdditions;
    public int dexAdditions;
    public int conAdditions;
    public int intAdditions;
    public int wisAdditions;
    public int chaAdditions;
    public int str;
    public int dex;
    public int con;
    public int intel;
    public int wis;
    public int cha;
    public int armorClass;
    public int passiveWis;
    public int availableSkillPoints;
    public int maxHP;
    public long id;
}

