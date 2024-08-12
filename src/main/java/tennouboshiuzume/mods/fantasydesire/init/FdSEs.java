package tennouboshiuzume.mods.fantasydesire.init;

import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import tennouboshiuzume.mods.fantasydesire.specialeffect.*;

public class FdSEs {
    public static ISpecialEffect CheatRumble = SpecialEffects.register(new CheatRumble());
    public static ISpecialEffect TyrantStrike = SpecialEffects.register(new TyrantStrike());
    public static ISpecialEffect SoulShield = SpecialEffects.register(new SoulShield());
}
