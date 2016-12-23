package com.departments.dto.data;

/**
 * Created by david on 15/08/16.
 */
public class Department {

        private final Integer id;
        private final String name;
        private final String creater;

        public Department(final Integer id, final String name, final String creater){
            this.id = id;
            this.name = name;
            this.creater = creater;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreater() {
            return creater;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Department other = (Department) obj;
            return this.id == other.id
                    && this.name == other.name
                    && this.creater == other.creater;
        }

        @Override
        public String toString(){
            return "Department [id = " + this.id + " name = "
                    + this.name + " creater = " + this.creater + "]";
        }
}

