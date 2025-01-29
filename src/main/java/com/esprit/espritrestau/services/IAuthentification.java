package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.TPA;

public interface IAuthentification  <T>{

    public boolean login(String email, String password, String type) ;
}
