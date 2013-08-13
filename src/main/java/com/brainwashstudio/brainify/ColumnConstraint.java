package com.brainwashstudio.orm;

import android.text.TextUtils;

public abstract class ColumnConstraint implements IQueryable {

    protected String mName = null;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public void query(StringBuilder builder) {
        if(mName != null) {
            builder.append("CONSTRAINT ").append(mName).append(" ");
        }
    }

    public static abstract class Check extends ColumnConstraint {
        // TODO implement "expr"

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);
            throw new RuntimeException("Not implemented yet!");
        }


    }

    public static class Collate extends ColumnConstraint {

        protected String mCollationName = null;

        public String getCollationName() {
            return mCollationName;
        }

        public void setCollationName(String collationName) {
            mCollationName = collationName;
        }

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);

            if(TextUtils.isEmpty(mCollationName)) {
                throw new IllegalStateException("Collation-name can't be null or empty!");
            }

            builder.append("COLLATE ").append(mCollationName);
        }
    }

    public static abstract class Default extends ColumnConstraint {

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);
            throw new RuntimeException("Not implemented yet!");
        }
    }

    public static abstract class ForeignKey extends ColumnConstraint {

        protected ForeignKeyClause mForeignKeyClause = new ForeignKeyClause();

        public ForeignKeyClause getForeignKeyClause() {
            return mForeignKeyClause;
        }

        public void setForeignKeyClause(ForeignKeyClause foreignKeyClause) {
            mForeignKeyClause = foreignKeyClause;
        }

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);

            mForeignKeyClause.query(builder);
        }
    }

    public static class NotNull extends ColumnConstraint {
        protected ConflictClause mConflictClause = new ConflictClause();

        public ConflictClause getConflictClause() {
            return mConflictClause;
        }

        public void setConflictClause(ConflictClause conflictClause) {
            mConflictClause = conflictClause;
        }

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);

            builder.append("NOT NULL ");

            mConflictClause.query(builder);
        }
    }


    public static class PrimaryKey extends ColumnConstraint {

        protected Direction mDirection = null;
        protected ConflictClause mConflictClause = new ConflictClause();
        protected boolean mAutoIncrement = false;

        public ConflictClause getConflictClause() {
            return mConflictClause;
        }

        public void setConflictClause(ConflictClause conflictClause) {
            mConflictClause = conflictClause;
        }

        public boolean isAutoIncrement() {
            return mAutoIncrement;
        }

        public void setAutoIncrement(boolean autoIncrement) {
            mAutoIncrement = autoIncrement;
        }

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);

            builder.append("PRIMARY KEY ");
            if(mDirection != null) {
                builder.append(mDirection.name()).append(" ");
            }
            mConflictClause.query(builder);
            if(mAutoIncrement) {
                builder.append(" AUTOINCREMENT");
            }
        }

        public static enum Direction {
            ASC, DESC
        }
    }

    public static class Unique extends ColumnConstraint {
        protected ConflictClause mConflictClause = new ConflictClause();

        public ConflictClause getConflictClause() {
            return mConflictClause;
        }

        public void setConflictClause(ConflictClause conflictClause) {
            mConflictClause = conflictClause;
        }

        @Override
        public void query(StringBuilder builder) {
            super.query(builder);

            builder.append("UNIQUE ");

            mConflictClause.query(builder);
        }
    }

}