package dk.ee.zg.common.weapon;

public final class WeaponManager {

    private static WeaponManager instance = new WeaponManager();

    private Weapon weaponSelected;


    private WeaponManager(){

    }

    public static WeaponManager getInstance(){
        return instance;
    }

    public Weapon getWeaponSelected() {
        return weaponSelected;
    }

    public void setWeaponSelected(Weapon weaponSelected) {
        this.weaponSelected = weaponSelected;
    }
}
