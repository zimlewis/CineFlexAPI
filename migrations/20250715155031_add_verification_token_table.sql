-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[VerificationToken] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Token] VARCHAR(36) NOT NULL UNIQUE,
    [CreatedTime] DATETIME,
    [ExpiredTime] DATETIME,
    [Verified] BIT NOT NULL DEFAULT 0,
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[VerificationToken];
-- +goose StatementEnd
