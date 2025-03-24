package bll.Validators;

import java.util.regex.Pattern;

import Model.Client;

/**
 * Validator pentru validarea numelui clienților.
 */
public class ClientName implements Validator<Client> {
    // Expresia regulată pentru validarea numelui
    private static final String NAME_PATTERN = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";

    /**
     * Validează numele clientului.
     * @param client Clientul al cărui nume trebuie validat.
     * @throws IllegalArgumentException dacă numele nu este valid.
     */
    public void validate(Client client) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        if (!pattern.matcher(client.getName()).matches()) {
            throw new IllegalArgumentException("Numele nu este valid!");
        }
    }
}
