# API
Для перевода с одного аккаунта на другой в рамках одного хозяина счетов (Participant):
запрос: 
HTTP POST + JSON
URL: http://localhost:8080/participant/innermoneyorder
тело запроса:
{
    "participantId": 1,
    "fromAccId": 1, //перевод с какого аккаунта
    "toAccId": 2, //на какой аккаунт
    "amount": 5 //количество денег для перевода - указано в копейка
    //TODO: в реальности лучше использовать BigInteger или BigDecimal
    //они позволяют фиксировать сколько символов после зарятой учитывать
    //и какое правило будет использоваться при округлении
}
тело ответа:
{
    "status": 1, //в ответе у нас возращается статус, 
    //который дает понять выполнился ли запрос, 
    //если status = 1 то запрос прошел успешно,
    //если = 2 - ошибка, текст ошибки пишется в теге 'message'
    //если ошибки не было, то это поле = null
    "message": null
}
---
если при переводе между аккаунтами: если у аккаунтов будут разные валюты,
то выведется текст ошибки. На данном этапе конвертация валют не реализована.
И перевод между аккаунтами можно осуществлять только, если аккаунты имеют одинаковую валюту.
например запрос:
URL: http://localhost:8080/participant/innermoneyorder
пример тела запроса:
{
    "participantId": 1,
    "fromAccId": 1,
    "toAccId": 4,
    "amount": 5
}
пример ответа с ошибкой:
{
    "status": 2,
    "message": "Currencies must be identical"
}
- будет выдан текст, что валюты должны быть одинаковыми.
//TODO в тексте ошибки не всегда тот текст, который явно указан на backend-е
//т.к. в коде у нас указано:
//Strings.isNotEmpty(e.getMessage()) ? e.getMessage() : "was Exception"
//то есть вероятность, что клиент получит сообщение с ошибкой, текст которого будет не понятен
//тут стоит доработать свои коды ошибок и свои тексты ошибок, чтобы у клиента всегда был вменяемый текст ошибки
---
для получения выписки по операциям 
необходимо использовать запрос:
URL http://localhost:8080/operation/getAllOperationsByAccId
пример тела запроса:
{
    "participantId": 1, //указываем, какой participant хочет получить выписку
    "accountId": 1 //и указываем выписку по какому счет хотим получить
}
пример ответа:
{
    "status": 1,
    "message": null,
    //вот список операций, которые были с "accountId": 1
    "operationList": [
        {
            "operationId": 1,
            "transactionId": 1,
            "accountId": 1,
            "type": 0, //этот тип дает нам понять - было это пополнение или списывание
            //PLUS(1), MINUS(0)
            //т.е. если type=1 это пополнение на счет, указанный в accountId
            //а если type=0 (как в данном примере) то это списывание с счета.
            "date": 1598822940,
            "amount": 5 //сумма, которая была списана/зачислена
        }
    ]
}
---
URL http://localhost:8080/operation/getAllOperationsByAccId
пример запроса:
{
    "participantId": 1,
    "accountId": 2
}
пример ответа:
{
    "status": 1,
    "message": null,
    "operationList": [
        {
            "operationId": 2,
            "transactionId": 1,
            "accountId": 2,
            "type": 1,
            "date": 1598822940,
            "amount": 5
        }
    ]
}
---
URL http://localhost:8080/operation/getAllOperationsByAccId
пример запроса:
{
    "participantId": 1,
    "accountId": 1
}
пример ответа:
{
    "status": 1,
    "message": null,
    "operationList": [
        {
            "operationId": 3,
            "transactionId": 3,
            "accountId": 1,
            "type": 0,
            "date": 1598823228,
            "amount": 15
        },
        {
            "operationId": 4,
            "transactionId": 3,
            "accountId": 1,
            "type": 1,
            "date": 1598823228,
            "amount": 15
        },
        {
            "operationId": 1,
            "transactionId": 1,
            "accountId": 1,
            "type": 0,
            "date": 1598822940,
            "amount": 5
        }
    ]
}
---
Для перечисления между разными владельцами (разными Participant):
URL http://localhost:8080/participant/externalMoneyOrder
пример тела запроса:
{
    "fromParticipantId": 1,
    "toParticipantId": 2,
    "fromAccId": 4,
    "toAccId": 3,
    "amount": 15
}
пример тела ответа:
{
    "status": 1,
    "message": null
}
проверяем:
тело запроса:
http://localhost:8080/operation/getAllOperationsByAccId
{
    "participantId": 2,
    "accountId": 3
}
тело ответа:
{
    "status": 1,
    "message": null,
    "operationList": [
        {
            "operationId": 2,
            "transactionId": 2,
            "accountId": 3,
            "type": 1,
            "date": 1598823714,
            "amount": 15
        }
    ]
}