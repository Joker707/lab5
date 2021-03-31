# Цели

- Ознакомиться с принципами и получить практические навыки разработки UI тестов для Android приложений.

## Задача 1 - Простейший UI тест

Ознакомимся с Espresso Framework.
Разработаем приложение, в котором есть одна кнопка и одно текстовое поле.
При первом нажатии на кнопку текст на кнопке должен меняться.

__Листинг 1.1 - UI тест__

    @RunWith(AndroidJUnit4::class)
    class ExampleInstrumentedTest {
        @get:Rule
        val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
        @Test
        fun testMainActivity() {
            val button = onView(withId(R.id.button))
            val editText = onView(withId(R.id.editText))
    
            editText.check(matches(withText("Hello World!")))
            button.check(matches(withText("Button")))
    
            button.perform(click()).check(matches(withText("Button clicked!!")))
            editText.perform(replaceText("Привет, мир!"))
    
            activityRule.scenario.onActivity { activity ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
    
            editText.check(matches(withText("Привет, мир!")))
            button.check(matches(withText("Button")))
        }
    }    



  - `@get:Rule` - получаем правило.
  - в первых 2 строчках создаем переменные;
  - следующие 2 строчки - проверяем на правильность начальных слов в элементах;
  - изменяем значения текстов;
  - поворачиваем экран (готовый код дали в тексте задания);
  - проверяем, что у нас теперь написано на кнопке и в текстовом поле - поле осталось без изменений, кнопка вернулась в исходное состояние.

Доступное и простое первое задание. Едем дальше составлять более приколдесные штуки!!

## Задача 2 - Тестирование навигации

Здесь я облегчил себе задачу и скопировал код из Task3 Lab3, где мы уже упростили код с помощью флагов,
и у нас не было необходимости юзать там методы с кодом результата :)

Представлю сначала весь листинг, а потом по кусочкам разберемся!

__Листинг 2.1 - Код более сложной тестовой штуки__

    @RunWith(AndroidJUnit4::class)
    class NavigationTest {
        @get:Rule
        val activityRule2 = ActivityTestRule(Activity1::class.java)
    
    
        private fun first() {
            onView(withId(R.id.button1)).check(matches(isDisplayed()))
            onView(withId(R.id.button2)).check(doesNotExist())
            onView(withId(R.id.button3)).check(doesNotExist())
            onView(withId(R.id.button4)).check(doesNotExist())
            onView(withId(R.id.button5)).check(doesNotExist())
        }
    
        private fun second() {
            onView(withId(R.id.button1)).check(doesNotExist())
            onView(withId(R.id.button2)).check(matches(isDisplayed()))
            onView(withId(R.id.button3)).check(matches(isDisplayed()))
            onView(withId(R.id.button4)).check(doesNotExist())
            onView(withId(R.id.button5)).check(doesNotExist())
        }
    
        private fun third() {
            onView(withId(R.id.button1)).check(doesNotExist())
            onView(withId(R.id.button2)).check(doesNotExist())
            onView(withId(R.id.button3)).check(doesNotExist())
            onView(withId(R.id.button4)).check(matches(isDisplayed()))
            onView(withId(R.id.button5)).check(matches(isDisplayed()))
        }
    
        @Test
        fun defaultNavigationTest() {
            first()
            onView(withId(R.id.button1)).perform(click())
            second()
            onView(withId(R.id.button2)).perform(click())
            first()
            onView(withId(R.id.button1)).perform(click())
            onView(withId(R.id.button3)).perform(click())
            third()
            onView(withId(R.id.button5)).perform(click())
            second()
            onView(withId(R.id.button2)).perform(click())
            first()
        }
    
        @Test
        fun destroyingTaskTest() {
            onView(withId(R.id.button1)).perform(click())
            onView(withId(R.id.button3)).perform(click())
            third()
            onView(withId(R.id.button4)).perform(click())
            onView(withId(R.id.button1)).perform(click())
            onView(withId(R.id.button2)).perform(click())
            first()
            try {
                pressBack()
            } catch (e: NoActivityResumedException) {
            }
            assertTrue(activityRule2.activity.isDestroyed)
        }
    
        @Test
        fun landscapeTest() {
            first()
            onView(withId(R.id.button1)).perform(click())
            activityRule2.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            second()
            onView(withId(R.id.button2)).perform(click())
            defaultNavigationTest()
            destroyingTaskTest()
        }
    }

  - Сначала так же берем правило(документация, правда рекомендовала юзать __ScenarioRule__ вместо __TestRule__,
но мы будем бунтарями, ибо у меня не получилось нормально проверить с первым правилом тот факт,
уничтожена активити или нет....)

  - Далее напишем проверки для каждой Activity - что у нее дествительно отображаются нужные кнопки
(Да, я считаю, что это хорошая проверка на подлинность активити!)

  - Следом мы проверим наш бэкстек: будем переходить между активити, периодически проверяя, что находимся в нужной,
  и после этого, когда мы вернемся в 1 активити, нажмем кнопочку Back, чтобы закрыть приложение и разрушить активити
  (в конце мы, естественно, проверяем, случилось ли это чудо)
  

  - Ну и в конечном итоге мы вернемся к тому, что нам показали в начале самой лабы - 
  проверка правильности работы в горизонтальной ориентации.
  В ней же заюзаем два уже ранеее написанных теста для усложнения(не просто так ведь мы их писали...)


# Выводы

Лабораторная оказалась очень легкой для меня и приятной в реализации. Хорошая передышка в середине курса лабораторных работ.
Я прямо проностальгировал про старым былым временам работы в Котоеде и написанию подобных тестов
для проверки правильности программы.
Надеюсь, дальше тоже будут какие-то разгрузочные штуки :)
Суммарно затраченное время - 3,5 часа с учетом отчёта. 
