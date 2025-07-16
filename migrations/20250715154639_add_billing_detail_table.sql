-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[BillingDetail] (
    [Id]            UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_BillingDetail         PRIMARY KEY,
    [Amount]        DECIMAL(20, 2),
    [CreatedTime]   DATETIME,
    [PaidTime]      DATETIME,
    [Paid]          BIT              NOT NULL CONSTRAINT DF_BillingDetail_Paid     DEFAULT 0,
    [Subscription]  UNIQUEIDENTIFIER            CONSTRAINT FK_BillingDetail_Subscription FOREIGN KEY REFERENCES [dbo].[Subscription]([Id]),
    [Account]       UNIQUEIDENTIFIER            CONSTRAINT FK_BillingDetail_Account      FOREIGN KEY REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[BillingDetail];
-- +goose StatementEnd
