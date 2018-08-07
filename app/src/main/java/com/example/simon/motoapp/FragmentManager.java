package com.example.simon.motoapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class FragmentManager {
    //Fragments
    MapsFragment g;
    NavigationFragment n;
    TwistiesFragment t;
    DirectionFragment d;
    ServoFragment s;
    FuelUpFragment f;
    SpeedoFragment sp;
    StatsFragment st;
    MotorcycleFragment m;

    android.support.v4.app.FragmentManager mFragmentManager;

    FragmentManager(android.support.v4.app.FragmentManager fm) {
        mFragmentManager = fm;
    }

    public void setFragment(int i)
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment frag = getFragment(i);
        //add fragment to container
        if(frag == null)
            fragmentTransaction.add(R.id.fragment_container, frag, Integer.toString(i));
        else
        {
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can reverse the transaction and bring back previous fragments by pressing the back button
            fragmentTransaction.replace(R.id.fragment_container, frag, Integer.toString(i));
            fragmentTransaction.addToBackStack(null);
        }
        //commit the transaction
        fragmentTransaction.commit();
    }

    public Fragment getFragment(int i)
    {
        switch(i)
        {
            case 0:
                if(g == null)
                    g = new MapsFragment();
                return g;
            case 1:
                if(n == null)
                    n = new NavigationFragment();
                return n;
            case 2:
                if(t == null)
                    t = new TwistiesFragment();
                return t;
            case 3:
                if(d == null)
                    d = new DirectionFragment();
                return d;
            case 4:
                if(s == null)
                    s = new ServoFragment();
                return s;
            case 5:
                if(f == null)
                    f = new FuelUpFragment();
                return f;
            case 6:
                if(sp == null)
                    sp = new SpeedoFragment();
                return sp;
            case 7:
                if(st == null)
                    st = new StatsFragment();
                return st;
            case 8:
                if(m == null)
                    m = new MotorcycleFragment();
                return m;
            default:
                return g = new MapsFragment();
        }
    }

}
