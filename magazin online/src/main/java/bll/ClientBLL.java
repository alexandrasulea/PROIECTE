package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import Model.Client;
import bll.Validators.ClientEmail;
import bll.Validators.ClientName;
import bll.Validators.ClientPhone;
import bll.Validators.Validator;
import dao.ClientDao;

/**
 * Logica de afaceri pentru operațiile Client.
 */
public class ClientBLL {


    public static final List<Validator<Client>> validators = new ArrayList<>();

    static {
        validators.add(new ClientEmail());
        validators.add(new ClientName());
        validators.add(new ClientPhone());
    }


}
