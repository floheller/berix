package org.floheller.berix.io.bjb;

public class BJBParseException extends Exception {
    private String msg;
    private fehlertypen number;
    public static enum fehlertypen  {MEHRERE_EINTRAEGE};
    public BJBParseException(String msg, fehlertypen number) {
        super(msg);
        this.msg=msg;
        this.number=number;
    }

    @Override
    public String getMessage() {
        return this.getErrorDescription()+" "+this.msg.trim();
    }

    private String getErrorDescription() {
        switch (this.number) {
            case MEHRERE_EINTRAEGE:
                return "Im JSONArray gibt es mehrere Listeneinträge. Behandelt werden derzeit nur Typen mit einem einzigen Eintrag.";
            default:
                return "Fehler unbekannt";
                    
        }
    }

    public static void throwMultipleListElementsError(String msg) throws BJBParseException {
        throw new BJBParseException(msg, fehlertypen.MEHRERE_EINTRAEGE);
    }


}
