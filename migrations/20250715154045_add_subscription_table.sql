-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Subscription] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [StartTime] DATETIME NOT NULL,
    [EndTime] DATETIME NOT NULL,
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Subscription];
-- +goose StatementEnd
