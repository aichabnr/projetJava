package com.esprit.espritrestau.services;

import com.esprit.espritrestau.entities.Presence;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ILignePresence<T> {

     void add(Presence p);

    Optional<T> getById(int id);

    List<Presence> getAll();

    boolean update(int id, Date date, int idRepas, int idConsomateur);

    boolean delete(int id);



}
