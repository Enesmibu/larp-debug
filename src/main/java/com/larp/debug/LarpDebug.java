package com.larp.debug;

import com.larp.debug.modules.LarpDebugV7;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class LarpDebug extends MeteorAddon {

    public static final Category CATEGORY = new Category("Larp Debug");

    @Override
    public void onInitialize() {
        Modules.get().add(new LarpDebugV7());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.larp.debug";
    }
}
