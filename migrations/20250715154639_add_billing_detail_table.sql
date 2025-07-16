-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[BillingDetail] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Amount] DECIMAL(20, 2),
    [CreatedTime] DATETIME,
    [PaidTime] DATETIME,
    [Paid] BIT DEFAULT 0,
    [Subscription] UNIQUEIDENTIFIER REFERENCES [dbo].[Subscription]([Id]),
    [Account] UNIQUEIDENTIFIER REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[BillingDetail];
-- +goose StatementEnd
