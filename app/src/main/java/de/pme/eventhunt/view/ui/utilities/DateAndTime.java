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
}
