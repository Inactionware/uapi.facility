package uapi.protocol.graphql.internal;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

public class GraphQLException extends ParameterizedException {

    public static final GraphQLExceptionBuilder builder() {
        return new GraphQLExceptionBuilder();
    }

    protected GraphQLException(ExceptionBuilder builder) {
        super(builder);
    }

    public static final class GraphQLExceptionBuilder
            extends ExceptionBuilder<GraphQLException, GraphQLExceptionBuilder> {

        public GraphQLExceptionBuilder() {
            super(GraphQLErrors.CATEGORY, new GraphQLErrors());
        }

        @Override
        protected GraphQLException createInstance() {
            return new GraphQLException(this);
        }
    }
}
