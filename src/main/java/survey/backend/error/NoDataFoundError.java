//package survey.backend.error;
//
//public class NoDataFoundError extends AbstractError {
//
//    public NoDataFoundError(String message){
//        super(message);
//    }
//
//public static NoDataFoundError withId(String itemType, int id){
//    return new NoDataFoundError(
//                    itemType
//                    + " with id "
//                    + id
//                    + " not found"
//    );
//}
//
//}

package survey.backend.error;

public class NoDataFoundError extends RuntimeException {

    public NoDataFoundError(String message) {
        super(message);
    }

    public static NoDataFoundError withId(String itemType, int id) {
        return new NoDataFoundError(
                itemType
                        + " with id "
                        + id
                        + " not found");
    }

    public static NoDataFoundError noResults(String itemType, String message) {
        return new NoDataFoundError(
                itemType
                        + " return 0 results"
                        + " with "
                        + message
        );
    }
}