package it.richkmeli.rms.data.rmc.model;

import it.richkmeli.jframework.database.DatabaseException;

import java.util.List;

public interface RMCModel {

    public boolean addRMC(RMC client) throws DatabaseException;

    public boolean editRMC(RMC client) throws DatabaseException;

    public boolean removeRMC(String user) throws DatabaseException;

    public List<RMC> getRMCs(String user) throws DatabaseException;

    public List<String> getAccounts(String rmcID) throws DatabaseException;

}
