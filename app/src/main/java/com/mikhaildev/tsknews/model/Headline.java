package com.mikhaildev.tsknews.model;


public class Headline {

    private long id;
    private String name;
    private String text;
    private PublicationDate publicationDate;
    private long bankInfoTypeId;


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public long getPublicationDate() {
        return publicationDate.getMilliseconds();
    }

    public long getBankInfoTypeId() {
        return bankInfoTypeId;
    }

    @Override
    public String toString() {
        return "Headline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", publicationDate=" + publicationDate +
                ", bankInfoTypeId=" + bankInfoTypeId +
                '}';
    }

    private static class PublicationDate {
        private long milliseconds;

        public long getMilliseconds() {
            return milliseconds;
        }

        @Override
        public String toString() {
            return "PublicationDate{" +
                    "milliseconds=" + milliseconds +
                    '}';
        }
    }
}
