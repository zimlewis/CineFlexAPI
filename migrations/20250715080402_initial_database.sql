-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Account]
(
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY, 
    [Username] NVARCHAR(100) NOT NULL UNIQUE,
    [Email] NVARCHAR(100) NOT NULL UNIQUE,
    [Password] NVARCHAR(100) NOT NULL,
    [CreatedTime] DATETIME DEFAULT CURRENT_TIMESTAMP,
    [UpdatedTime] DATETIME,
    [Verify] BIT DEFAULT 0,
    [Role] INT DEFAULT 0,
    [Activate] BIT NOT NULL DEFAULT 1
);

-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Account];
-- +goose StatementEnd
