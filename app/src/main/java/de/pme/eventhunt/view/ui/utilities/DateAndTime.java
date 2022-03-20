package de.pme.eventhunt.view.ui.utilities;

import java.time.LocalDateTime;
// class for managing localdatetime and generating Strings
public class DateAndTime {
        public String day = "";;
        public String month = "";;
        public String year = "";;
        public String hour = "";;
        public String minute = "";;

        public void setDateAndTime(LocalDateTime localDateTime)
        {
                Integer yearInt = localDateTime.getYear();
                String yearString = yearInt.toString();
                this.year = yearString;

                Integer monthInt = localDateTime.getMonthValue();
                if(monthInt < 10) this.month = "0" + monthInt;
                else this.month = monthInt.toString();

                Integer dayInt = localDateTime.getDayOfMonth();
                if(dayInt < 10) this.day = "0" + dayInt;
                else this.day = dayInt.toString();

                Integer hourInt = localDateTime.getHour();
                if(hourInt < 10) this.hour = "0" + hourInt;
                else this.hour = hourInt.toString();

                Integer minuteInt = localDateTime.getMinute();
                if(minuteInt < 10) this.minute = "0" + minuteInt;
                else this.minute = minuteInt.toString();
        }



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
