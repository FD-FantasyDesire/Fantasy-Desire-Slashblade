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
        ItemSlashBlade.specialAttacks.put(203, new CrossSaberSA());
        ItemSlashBlade.specialAttacks.put(204, new ShinMagnetStormSword());
        ItemSlashBlade.specialAttacks.put(205, new RainOfRainbow());
        ItemSlashBlade.specialAttacks.put(206, new SpikeOfAbyss());
        ItemSlashBlade.specialAttacks.put(207, new CrimsonStorm());
        ItemSlashBlade.specialAttacks.put(208, new TwinSystemL());
        ItemSlashBlade.specialAttacks.put(209, new TwinSystemR());
        ItemSlashBlade.specialAttacks.put(210, new FreezeZero());
        ItemSlashBlade.specialAttacks.put(211, new RainbowStar());
        ItemSlashBlade.specialAttacks.put(212, new ABS());
        ItemSlashBlade.specialAttacks.put(213, new ABMS());
        ItemSlashBlade.specialAttacks.put(1000, new tesSA());
        ItemSlashBlade.specialAttacks.put(Integer.MAX_VALUE,new StatMultipler());
//            System.out.println("SA registed!");
    }
}