package com.example.agust.gestordeejercicio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Rutinas();
            case 1: return new Cronometro();
            case 2: return new Metas();
            default: return new Rutinas();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
