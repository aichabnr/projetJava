package com.esprit.espritrestau.services;

import java.util.List;

public interface IAbonnement <T>{
    void addAbonnement(T abonnement);

    T getAbonnementById(int id);

    List<T> getAllAbonnements();

    void updateAbonnement(T abonnement);

    void deleteAbonnement(int id);

    void consommerRepas(int abonnementId,int repasId) ;
}
