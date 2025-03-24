package bll.Validators;

import java.util.regex.Pattern;

import Model.Client;

/**
 * Validator pentru validarea numerelor de telefon ale clienților.
 */
public class ClientPhone implements Validator<Client> {
    // Expresia regulată pentru validarea numerelor de telefon
    private static final String PHONE_PATTERN = "^\\+?[0-9()-]+$";

    /**
     * Validează numărul de telefon al clientului.
     * @param client Clientul al cărui număr de telefon trebuie validat.
     * @throws IllegalArgumentException dacă numărul de telefon nu este valid.
     */
    public void validate(Client client) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        if (!pattern.matcher(client.getPhone()).matches()) {
            throw new IllegalArgumentException("Numărul de telefon nu este valid!");
        }
    }
}
