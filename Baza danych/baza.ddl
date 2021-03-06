DROP TABLE AIRLINES CASCADE CONSTRAINTS ;

DROP TABLE AIRPORT CASCADE CONSTRAINTS ;

DROP TABLE FLIGHT CASCADE CONSTRAINTS ;

DROP TABLE PASSANGER CASCADE CONSTRAINTS ;

DROP TABLE PASSANGER_LIST CASCADE CONSTRAINTS ;

DROP TABLE PERSON CASCADE CONSTRAINTS ;

DROP TABLE PILOT CASCADE CONSTRAINTS ;

DROP TABLE PLANE CASCADE CONSTRAINTS ;

DROP TABLE TRAFFIC_CONTROLLER CASCADE CONSTRAINTS ;

CREATE TABLE AIRLINES
  (
    ID      NUMBER NOT NULL ,
    NAME             VARCHAR2 (20) NOT NULL ,
    ESTABLISHED_DATE DATE NOT NULL ,
    HQ_LOCATION      VARCHAR2 (30) NOT NULL
  ) ;
ALTER TABLE AIRLINES ADD CONSTRAINT AIRLINES_PK PRIMARY KEY ( ID ) ;


CREATE TABLE AIRPORT
  (
    ID                NUMBER NOT NULL ,
    NAME                      VARCHAR2 (60) NOT NULL ,
    CITY                      VARCHAR2 (30) NOT NULL ,
    COUNTRY                   VARCHAR2 (30) NOT NULL ,
    TRAFFIC_CONTROLLER        NUMBER UNIQUE NOT NULL
  ) ;
ALTER TABLE AIRPORT ADD CONSTRAINT AIRPORT_PK PRIMARY KEY ( ID ) ;


CREATE TABLE FLIGHT
  (
    ID               NUMBER NOT NULL ,
    ARRIVAL_TIME     TIMESTAMP NOT NULL ,
    DEPARTURE_TIME   TIMESTAMP NOT NULL ,
    RESERVED_SEAT_NO NUMBER DEFAULT 0,
    SOURCE           NUMBER NOT NULL ,
    DESTINATION      NUMBER NOT NULL ,
    PLANE            NUMBER NOT NULL ,
    PILOT            NUMBER NOT NULL
  ) ;
ALTER TABLE FLIGHT ADD CONSTRAINT FLIGHT_PK PRIMARY KEY ( ID ) ;


CREATE TABLE PASSANGER
  (
    ID                NUMBER NOT NULL,
    PASSPORT_NUMBER    NUMBER UNIQUE NOT NULL,
    ACCOUNT_LOGIN      VARCHAR2 (60) UNIQUE NOT NULL ,
    ACCOUNT_PASSWORD   VARCHAR2 (32) NOT NULL ,
    CREDIT_CARD_NUMBER NUMBER NOT NULL
  ) ;
ALTER TABLE PASSANGER ADD CONSTRAINT PASSANGER_PK PRIMARY KEY ( ID ) ;


CREATE TABLE PASSANGER_LIST
  (
    FLIGHT_ID                 NUMBER NOT NULL ,
    PASSANGER_ID              NUMBER NOT NULL
  ) ;
ALTER TABLE PASSANGER_LIST ADD CONSTRAINT PASSAN_LIST_PK PRIMARY KEY ( FLIGHT_ID, PASSANGER_ID ) ;


CREATE TABLE PERSON
  (
    PASSPORT_NUMBER NUMBER NOT NULL ,
    FIRST_NAME      VARCHAR2 (20) NOT NULL ,
    LAST_NAME       VARCHAR2 (20) NOT NULL ,
    SEX             CHAR (1) DEFAULT '?' NOT NULL ,
    BIRTH_DATE      DATE NOT NULL ,
    COUNTRY     VARCHAR2 (30) NOT NULL,
    CHECK (SEX IN('?', 'F', 'M'))
  ) ;
ALTER TABLE PERSON ADD CONSTRAINT PERSON_PK PRIMARY KEY ( PASSPORT_NUMBER ) ;


CREATE TABLE PILOT
  (
    LICENCE_NUMBER  NUMBER NOT NULL ,
    PASSPORT_NUMBER NUMBER NOT NULL ,
    AIRLINES_ID     NUMBER NOT NULL,
    EXPERIENCE      NUMBER NOT NULL ,
    SALARY          NUMBER DEFAULT '0' NOT NULL,
    CHECK (SALARY >= 0)
  ) ;
ALTER TABLE PILOT ADD CONSTRAINT PILOT_PK PRIMARY KEY ( LICENCE_NUMBER ) ;


CREATE TABLE PLANE
  (
    PLANE_ID     NUMBER NOT NULL ,
    MANUFACTURER VARCHAR2 (20) NOT NULL ,
    MODEL        VARCHAR2 (20) NOT NULL ,
    DATE_OF_PROD DATE NOT NULL ,
    AIRLINES_ID  NUMBER NOT NULL ,
    CAPACITY     NUMBER NOT NULL
  ) ;
ALTER TABLE PLANE ADD CONSTRAINT PLANE_PK PRIMARY KEY ( PLANE_ID ) ;


CREATE TABLE TRAFFIC_CONTROLLER
  (
    LICENCE_NUMBER    NUMBER NOT NULL ,
    PASSPORT_NUMBER   NUMBER NOT NULL ,
    UNIVERSITY_DEGREE VARCHAR2 (20) NOT NULL ,
    SALARY          NUMBER DEFAULT '0' NOT NULL,
    EMERGENCY_NUMBER  NUMBER NOT NULL,
    CHECK (SALARY >= 0)
  ) ;
ALTER TABLE TRAFFIC_CONTROLLER ADD CONSTRAINT TRAFFIC_CONTROLLER_PK PRIMARY KEY ( LICENCE_NUMBER ) ;


ALTER TABLE AIRPORT ADD CONSTRAINT AIRPORT_CONTROLLER_FK FOREIGN KEY ( TRAFFIC_CONTROLLER ) REFERENCES TRAFFIC_CONTROLLER ( LICENCE_NUMBER ) ;

ALTER TABLE PASSANGER_LIST ADD CONSTRAINT FK_ASS_6 FOREIGN KEY ( PASSANGER_ID ) REFERENCES PASSANGER ( ID ) ;

ALTER TABLE PASSANGER_LIST ADD CONSTRAINT FK_ASS_7 FOREIGN KEY ( FLIGHT_ID ) REFERENCES FLIGHT ( ID ) ;

ALTER TABLE FLIGHT ADD CONSTRAINT FLIGHT_AIRPORT_FK FOREIGN KEY ( SOURCE ) REFERENCES AIRPORT ( ID ) ;

ALTER TABLE FLIGHT ADD CONSTRAINT FLIGHT_AIRPORT_FKv1 FOREIGN KEY ( DESTINATION ) REFERENCES AIRPORT ( ID ) ;

ALTER TABLE FLIGHT ADD CONSTRAINT FLIGHT_PILOT_FK FOREIGN KEY ( PILOT ) REFERENCES PILOT ( LICENCE_NUMBER ) ;

ALTER TABLE FLIGHT ADD CONSTRAINT FLIGHT_PLANE_FK FOREIGN KEY ( PLANE ) REFERENCES PLANE ( PLANE_ID ) ;

ALTER TABLE PASSANGER ADD CONSTRAINT PASSANGER_FK FOREIGN KEY ( PASSPORT_NUMBER ) REFERENCES PERSON ( PASSPORT_NUMBER ) ;

ALTER TABLE PILOT ADD CONSTRAINT PILOT_AIRLINES_FK FOREIGN KEY ( AIRLINES_ID ) REFERENCES AIRLINES ( ID ) ;

ALTER TABLE PILOT ADD CONSTRAINT PILOT_PERSON_FK FOREIGN KEY ( PASSPORT_NUMBER ) REFERENCES PERSON ( PASSPORT_NUMBER ) ;

ALTER TABLE PLANE ADD CONSTRAINT PLANE_AIRLINES_FK FOREIGN KEY ( AIRLINES_ID ) REFERENCES AIRLINES ( ID ) ;

ALTER TABLE TRAFFIC_CONTROLLER ADD CONSTRAINT TRAF_CONTROL_FK FOREIGN KEY ( PASSPORT_NUMBER ) REFERENCES PERSON ( PASSPORT_NUMBER ) ;
