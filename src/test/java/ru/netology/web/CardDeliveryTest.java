package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SetValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardDeliveryTest {

    public static String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestV1() /* Все значения валидны */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title").shouldHave(text("Успешно!"), Duration.ofMillis(15_000)).shouldBe(visible);
        $("[data-test-id='notification'] .notification__content").shouldHave(text("Встреча успешно забронирована на " + date), Duration.ofMillis(15_000)).shouldBe(visible);
    }

    @Test
    void shouldTestV2() /* Город, не являюющийся административным центром cубъекта РФ */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Энгельс");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestV3() /* Пустое поле ввода "город" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestV4() /* Ранее трех дней с текущей даты */ {
        String date = date(2);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestV5() /* Пустое поле ввода "дата" */ {
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldTestV6() /* Ввод латиницей в поле "Фамилия и Имя" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Petrov Ivan");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    void shouldTestV7() /* Ввод чисел в поле "Фамилия и Имя" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван1");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    void shouldTestV8() /* Ввод символов в поле "Фамилия и Имя" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров.Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    void shouldTestV9() /* Пустое поле ввода "Фамилия и Имя" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestV10() /* Ввод 11 цифр без "+" в поле "Мобильный телефон" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("79117774325");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @Test
    void shouldTestV11() /* Ввод 10 цифр в поле "Мобильный телефон" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+7911777432");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @Test
    void shouldTestV12() /* Ввод 12 цифр в поле "Мобильный телефон" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+791177743251");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"));
    }

    @Test
    void shouldTestV13() /* Пустое поле ввода "Мобильный телефон" */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestV14() /* Не стоит галочка в чек-боксе */ {
        String date = date(3);
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").doubleClick().sendKeys(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79117774325");
        $$("button").find(exactText("Забронировать")).click();
        boolean agreement = $("[data-test-id='agreement']").isDisplayed();
        assertEquals(true, agreement);
    }

}