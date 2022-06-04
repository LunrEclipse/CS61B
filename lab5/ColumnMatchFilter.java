/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _col1 = input.colNameToIndex(colName1);
        _col2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        if(_next.getValue(_col1).equals(_next.getValue(_col2))) {
            return true;
        }
        return false;
    }

    private int _col1;
    private int _col2;
}
