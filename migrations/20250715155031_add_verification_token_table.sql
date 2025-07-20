-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[VerificationToken] (
    [Id]         UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_VerificationToken         PRIMARY KEY,
    [Token]      VARCHAR(36)      NOT NULL CONSTRAINT UQ_VerificationToken_Token   UNIQUE,
    [CreatedTime]DATETIME,
    [ExpiredTime]DATETIME,
    [Verified]   BIT              NOT NULL CONSTRAINT DF_VerificationToken_Verified DEFAULT 0,
    [Account]    UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_VerificationToken_Account FOREIGN KEY REFERENCES [dbo].[Account]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[VerificationToken];
-- +goose StatementEnd
