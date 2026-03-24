package com.meteorui.client.module;

import com.meteorui.client.gui.Category;
import com.meteorui.client.module.modules.render.FullbrightModule;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {}
    public static ModuleManager getInstance() { return INSTANCE; }

    public void init() {
        register(new FullbrightModule());
    }

    private void register(Module module) { modules.add(module); }
    public List<Module> getModules() { return modules; }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) if (m.getCategory() == category) result.add(m);
        return result;
    }

    public Module getModule(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
}
