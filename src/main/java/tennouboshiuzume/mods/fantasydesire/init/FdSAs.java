package tennouboshiuzume.mods.fantasydesire.init;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import tennouboshiuzume.mods.fantasydesire.specialattack.*;

public class FdSAs {
    public  FdSAs(){
        loadSAs();
    }
    public void loadSAs(){
        ItemSlashBlade.specialAttacks.put(200, new WingToTheFutureSA());
        ItemSlashBlade.specialAttacks.put(201, new ChargeShot());
        ItemSlashBlade.specialAttacks.put(202, new OverCharge());
//        ItemSlashBlade.specialAttacks.put(201, new testSA02());
//        ItemSlashBlade.specialAttacks.put(202, new tesSA());
        ItemSlashBlade.specialAttacks.put(203, new CrossSaberSA());
        ItemSlashBlade.specialAttacks.put(204, new ShinMagnetStormSword());
        ItemSlashBlade.specialAttacks.put(205, new RainOfRainbow());
        ItemSlashBlade.specialAttacks.put(206, new SpikeOfAbyss());
//            System.out.println("SA registed!");
    }
}