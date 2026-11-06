package org.floheller.berix.io.bsbj;

public class BSBJParseException extends Exception {
    private String msg;
    private fehlertypen number;
    public static enum fehlertypen  {MEHRERE_EINTRAEGE};
    public BSBJParseException(String msg, fehlertypen number) {
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

    public static void throwMultipleListElementsError(String msg) throws BSBJParseException {
        throw new BSBJParseException(msg, fehlertypen.MEHRERE_EINTRAEGE);
    }


}
