DECLARE
  birthdate DATE := to_date('03/11/2011', 'dd/mm/yyyy');
  type s_array is varray(13) of varchar2(20);
  type t_array is varray(2) of varchar2(1);
  name_array s_array := s_array('Matt', 'Joanne', 'Robert', 'Robert', 'Susan', 'Samuel', 'Michael', 'Paula', 'Lois', 'Jose', 'Dennis','Terry', 'Martha');
  lastname_array s_array := s_array('Hawkins', 'Ferguson', 'Sanders', 'Little', 'Vasquez', 'Fisher', 'Ward', 'Hall', 'Hunt', 'Reid', 'Davis', 'Greene', 'Stone');
  country_array s_array := s_array('Maldives','Indonesia','United States','Russia','Brazil','Tanzania','China','Argentina','Indonesia','France','Poland','Germany','Japon');
  sex_array t_array := t_array('M', 'F');
BEGIN
FOR i IN 1..10000 LOOP
  birthdate := TO_DATE(TRUNC(DBMS_RANDOM.VALUE(TO_CHAR(DATE '1910-01-01','J')
                                      ,TO_CHAR(DATE '2017-01-21','J'))),'J');
  insert into PERSON(PASSPORT_NUMBER, FIRST_NAME, LAST_NAME, SEX, BIRTH_DATE, COUNTRY)  
    values(10000 + i, name_array(round(dbms_random.value(1,13))), lastname_array(round(dbms_random.value(1,13))), sex_array(mod(i,2) + 1), birthdate, country_array(round(dbms_random.value(1,13))));
  insert into PILOT (LICENCE_NUMBER, PASSPORT_NUMBER, AIRLINES_ID, EXPERIENCE, SALARY) 
    values (1000 + i, 10000 + i, round(dbms_random.value(1,10)), round(dbms_random.value(0,25)), round(dbms_random.value(1000,30000)));
END LOOP;
END;