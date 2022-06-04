/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _match = match;
        _col = input.colNameToIndex(colName);

    }

    @Override
    protected boolean keep() {
        if(_next.getValue(_col).equals(_match)) {
            return true;
        }
        return false;
    }

   private String _match;
    private int _col;
}
