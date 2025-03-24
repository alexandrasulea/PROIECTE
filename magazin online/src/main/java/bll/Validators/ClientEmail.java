package bll.Validators;

import java.util.regex.Pattern;

import Model.Client;

/**
 * Validator pentru validarea adreselor de email ale clienților.
 */
public class ClientEmail implements Validator<Client> {
    // Expresia regulată pentru validarea adreselor de email
    private static final String EMAIL_PATTERN ="^[a-zA-Z0-9. _%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";

    /**
     * Validează adresa de email a clientului.
     * @param client Clientul al cărui email trebuie validat.
     * @throws IllegalArgumentException dacă adresa de email nu este validă.
     */
    public void validate(Client client) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(client.getEmail()).matches()) {
            throw new IllegalArgumentException("Emailul nu este o adresă de email validă!");
        }
    }
}
