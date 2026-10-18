package coursehub.practice6.time;

public class InvalidDateFieldException extends RuntimeException {
    private final String fieldName, inputValue;
    public InvalidDateFieldException(String fieldName, String inputValue, Throwable cause) {
        super("Некорректное поле '" + fieldName + "', значение: '" + inputValue + "'", cause);
        this.fieldName = fieldName; this.inputValue = inputValue;
    }
    public String getFieldName() { return fieldName; }
    public String getInputValue() { return inputValue; }
}
