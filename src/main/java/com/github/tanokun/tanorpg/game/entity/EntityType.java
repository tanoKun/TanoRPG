package com.github.tanokun.tanorpg.game.entity;

import com.github.tanokun.tanorpg.game.entity.base.*;

public enum EntityType {
    //BABY_DROWNED(BaseBabyDrowned.class),
    //BABY_HUSK(BaseBabyHusk.class),
    //BABY_PIG_ZOMBIE(BaseBabyPigZombie.class),
    //BABY_ZOMBIE(BaseBabyZombie.class),
    //BABY_ZOMBIE_VILLAGER(BaseBabyZombieVillager.class),
    //BAT(BaseBat.class),
    //BLAZE(BaseBlaze.class),
    //CAVE_SPIDER(BaseCaveSpider.class),
    //CHICKEN(BaseChicken.class),
    //CAT(BaseCat.class),
    //COD(BaseCod.class),
    //COW(BaseCow.class),
    //CREEPER(BaseCreeper.class),
    //DOLPHIN(BaseDolphin.class),
    //DONKEY(BaseDonkey.class),
    //DROWNED(BaseDrowned.class),
    //ELDER_GUARDIAN(BaseElderGuardian.class),
    //ENDERMAN(BaseEnderman.class),
    //ENDERMITE(BaseEndermite.class),
    //EVOKER(BaseEvoker.class),
    //FOX(BaseFox.class),
    //GHAST(BaseGhast.class),
    //GIANT(BaseGiant.class),
    GUARDIAN(BaseGuardian.class),
    //HORSE(BaseHorse.class),
    HUSK(BaseHusk.class),
    //ILLUSIONER(BaseIllusioner.class),
    //IRON_GOLEM(BaseIronGolem.class),
    //LLAMA(BaseLlama.class),
    //MAGMA_CUBE(BaseMagmaCube.class),
    //MULE(BaseMule.class),
    //MUSHROOM_COW(BaseMushroomCow.class),
    //OCELOT(BaseOcelot.class),
    //PANDA(BasePanda.class),
    //PARROT(BaseParrot.class),
    //PHANTOM(BasePhantom.class),
    //PIG(BasePig.class),
    //PIG_ZOMBIE(BasePigZombie.class),
    //PILLAGER(BasePillager.class),
    //POLAR_BEAR(BasePolarBear.class),
    //RABBIT(BaseRabbit.class),
    //RAVAGER(BaseRavager.class),
    //SHEEP(BaseSheep.class),
    SKELETON(BaseSkeleton.class),
    //SKELETON_HORSE(BaseSkeletonHorse.class),
    SLIME(BaseSlime.class),
    SPIDER(BaseSpider.class),
    STRAY(BaseStray.class),
    //VEX(BaseVex.class),
    //VINDICATOR(BaseVindicator.class),
    //WITCH(BaseWitch.class),
    WITHER_SKELETON(BaseWitherSkeleton.class),
    //WOLF(BaseWolf.class),
    ZOMBIE(BaseZombie.class),
    //ZOMBIE_HORSE(BaseZombieHorse.class),
    //ZOMBIE_VILLAGER(BaseZombieVillager.class)
    ;

    private final Class<? extends ObjectEntity> CLASS;

    EntityType(Class<? extends ObjectEntity> clazz) {
        CLASS = clazz;
    }

    public Class<? extends ObjectEntity> getClazz() {
        return CLASS;
    }
}