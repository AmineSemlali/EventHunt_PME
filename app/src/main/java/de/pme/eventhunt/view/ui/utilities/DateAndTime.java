package de.pme.eventhunt.view.ui.utilities;

import java.time.LocalDateTime;

public class DateAndTime {
        public String day = "";
        public String month = "";
        public String year = "";
        public String hour = "";
        public String minute = "";

        public LocalDateTime toLocalDateTime()
        {
                LocalDateTime localDateTime = LocalDateTime.parse(  this.year
                        + "-" + this.month
                        + "-" + this.day
                        + "T" + this.hour
                        + ":" + this.minute
                        + ":00"             );

                return localDateTime;
        }

        public String toLocalDateTimeString()
        {
                return (this.year
                        + "-" + this.month
                        + "-" + this.day
                        + "T" + this.hour
                        + ":" + this.minute
                        + ":00"             );
        }

        public String formatString()
        {
                if(!this.year.isEmpty() && !this.month.isEmpty() && !this.day.isEmpty() && !this.hour.isEmpty() && !this.minute.isEmpty())
                {
                        String returnString = this.day + "." + this.month + "." + this.year + "   " + this.hour + ":" + this.minute;
                        return returnString;
                }
                else return "Error in DateAndTime::formatString";
        }

        public DateAndTime(String localDateTimeString) throws Exception {
                if(localDateTimeString.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:[\\d]{2}"))
                {
                        String[] parts = localDateTimeString.split("-|T|:");
                        this.year = parts[0];
                        this.month = parts[1];
                        this.day = parts[2];
                        this.hour = parts[3];
                        this.minute = parts[4];
                }
                else
                {
                        throw new Exception("Date String not correct!");
                }
        }

        public boolean isComplete(){
                if(year.isEmpty() || month.isEmpty() || day.isEmpty() || hour.isEmpty() || minute.isEmpty())
                {
                        return false;
                }
                else return true;
        }

        public DateAndTime(){};
}
