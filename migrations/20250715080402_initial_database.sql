-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Account]
(
    [Id] UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Account PRIMARY KEY,
    [Username] NVARCHAR(100) NOT NULL CONSTRAINT UQ_Account_Username UNIQUE,
    [Email] NVARCHAR(100) NOT NULL CONSTRAINT UQ_Account_Email UNIQUE,
    [Password] NVARCHAR(100) NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Verify] BIT CONSTRAINT DF_Account_Verify DEFAULT 0,
    [Role] INT CONSTRAINT DF_Account_Role DEFAULT 0,
    [Activate] BIT NOT NULL CONSTRAINT DF_Account_Activate DEFAULT 1
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Account];
-- +goose StatementEnd
