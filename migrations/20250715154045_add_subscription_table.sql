-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Subscription] (
    [Id]        UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Subscription     PRIMARY KEY,
    [StartTime] DATETIME         NOT NULL,
    [EndTime]   DATETIME         NOT NULL,
    [Account]   UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Subscription_Account FOREIGN KEY REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Subscription];
-- +goose StatementEnd
