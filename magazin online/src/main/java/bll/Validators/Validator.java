package bll.Validators;

/**
 * Interfață pentru validatorii entităților.
 * @param <T> Tipul entității de validat.
 */
public interface Validator<T> {
    /**
     * Validează entitatea.
     * @param t Entitatea de validat.
     */
    public void validate(T t);
}
