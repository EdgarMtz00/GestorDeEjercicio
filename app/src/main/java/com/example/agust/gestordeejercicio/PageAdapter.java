package com.example.agust.gestordeejercicio;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position:
     * Numero del item a regresar
     * @return:
     * Fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new RutinasFragment();
            case 1: return new Cronometro();
            case 2: return new Metas();
            case 3: return new ConfiguracionFragment();
            default: return new RutinasFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Rutinas";
            case 1: return "Cronometro";
            case 2: return "Metas";
            case 3: return "configuracion";
            default: return  "tienes un error en el pager";
        }
    }

    @Override
    public int getCount() {return 4;}
}
