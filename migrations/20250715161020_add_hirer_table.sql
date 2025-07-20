-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Hirer] (
    [Id]         UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Hirer         PRIMARY KEY,
    [Alias]      NVARCHAR(50)     NOT NULL,
    [Email]      VARCHAR(50)      NOT NULL CONSTRAINT UQ_Hirer_Email    UNIQUE,
    [Phone]      VARCHAR(15)      NOT NULL CONSTRAINT UQ_Hirer_Phone    UNIQUE,
    [CreatedTime]DATETIME,
    [UpdatedTime]DATETIME
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Hirer];
-- +goose StatementEnd
