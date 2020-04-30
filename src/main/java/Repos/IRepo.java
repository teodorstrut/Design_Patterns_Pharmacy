package Repos;

import Exceptions.RepoException;
import Models.BaseObject;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IRepo<T> {
    T add(T elem) throws RepoException;

    int delete(int elemId) throws RepoException;

    T update(T elem) throws RepoException;

    ArrayList<T> getAll() throws RepoException;
}
