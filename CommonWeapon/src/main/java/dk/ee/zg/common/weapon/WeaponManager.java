package dk.ee.zg.common.weapon;

public final class WeaponManager {
    /**
     * {@link WeaponManager} instance of weapon manager class,
     * used for making it a singleton pattern.
     */
    private static WeaponManager instance;

    /**
     * {@link Weapon} instance of weapon class,
     * used for making it a singleton pattern.
     */
    private Weapon weaponSelected;


    private WeaponManager() {

    }

    /**
     * @return - returns an instance of WeaponManager
     */
    public static WeaponManager getInstance() {
        if (instance == null) {
            instance = new WeaponManager();
        }
        return instance;
    }

    public Weapon getWeaponSelected() {
        return weaponSelected;
    }

    public void setWeaponSelected(final Weapon weapon) {
        this.weaponSelected = weapon;
    }
}
